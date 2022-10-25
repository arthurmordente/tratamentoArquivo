import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


public class Intercalar{

    public static void deleteFiles() { // Deleta os arquivos já existentes
        File file1 = new File("arq1.bin");
        File file2 = new File("arq2.bin");
        File file3 = new File("arq3.bin");
        File file4 = new File("arq4.bin");
        File file5 = new File("arqFinal.bin");
    
        file1.delete();
        file2.delete();
        file3.delete();
        file4.delete();
        file5.delete();
    }

    public static Conta read0(RandomAccessFile raf, int idPesquisado){
        
        try{
            Conta novaConta = new Conta();

            raf.seek(0);//Ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()){//Percorro todo o arquivo
                char lapide = raf.readChar();
                if(lapide == ' '){//Verifico se a lapide não esta ativa
                    int tamanhoArq = raf.readInt();//Armazeno o tamnho do registro
                    novaConta.setIdConta(raf.readInt());//Seto o id da conta para verificar 
                    if(novaConta.getIdConta() == idPesquisado){//Consulta para verificar se o id da primeira conta é o desejado
                        novaConta.setNomePessoa(raf.readUTF());
                        ArrayList<String> arrayTmp = new ArrayList<String>();
                        int qtdEmail = raf.readInt();
                        for(int i=0 ; i<qtdEmail ; i++){
                            arrayTmp.add(raf.readUTF());
                        }
                        novaConta.setEmail(arrayTmp);
                        novaConta.setNomeUsuario(raf.readUTF());
                        novaConta.setSenha(raf.readUTF());
                        novaConta.setCpf(raf.readUTF());
                        novaConta.setCidade(raf.readUTF());
                        novaConta.setTransferenciasRealizadas(raf.readInt());
                        novaConta.setSaldoConta(raf.readFloat());

                        return novaConta;
                    }
                    else{//Se o id da conta não for o requerido
                        raf.skipBytes(tamanhoArq -4);//Pulo o registro inteiro
                    }
                }
                else{
                    int tamanhointeiro = raf.readInt();
                    raf.skipBytes(tamanhointeiro);//Pulo o registro ja que a lápide vai estar desativada
                }
            }
            return null;
        }catch(Exception e){
            System.out.println("Falha ao ler o registro...");
            return null;
        }
    }


    public static boolean intercalacao(RandomAccessFile raf, int limite)throws IOException{

        deleteFiles();//Deleta os arquivos já existentes
        ArrayList<Conta> contas = new ArrayList<Conta>();
        RandomAccessFile arq1 = new RandomAccessFile("arq1.bin", "rw");
        RandomAccessFile arq2 = new RandomAccessFile("arq2.bin", "rw");
        RandomAccessFile arq3 = new RandomAccessFile("arq3.bin", "rw");
        RandomAccessFile arq4 = new RandomAccessFile("arq4.bin", "rw");
        RandomAccessFile arqFinal = new RandomAccessFile("arqFinal.bin", "rw");
        int voltas = 0;

        try{
            raf.seek(4);//Ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()){//Percorro todo o arquivo
                Conta novaConta = new Conta();
                if(raf.readChar() == ' '){//Verifico se a lapide esta ativa
                    int tamanhoArq = raf.readInt();//Armazeno o tamnho do arquivo
                    int idRescue = raf.readInt();//Seto o id
                    novaConta = CRUD.read(raf, idRescue);
                    contas.add(novaConta);
                }
                else{
                    raf.skipBytes(raf.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
        }catch(Exception e){
            System.out.println("Erro ao ler arquivo !");
        }

        //-------------------- PRIMEIRA DISTRIBUIÇÃO --------------------

        while(contas.size() > 0){//Enquanto o arraylist não estiver vazio
            voltas++;
            ArrayList<Conta> contaTmp = new ArrayList<Conta>();
            for(int i=0 ; i<limite ; i++){
                if(contas.size() > 0){//Verifico se o arraylist não está vazio
                    contaTmp.add(contas.get(0));//Adiciono o elemento no arraylist temporario
                    contas.remove(0);//Removo o elemento do arraylist principal
                }
            }

            Collections.sort(contaTmp);//Ordeno o arraylist temporario

            int tamanhoTmp = contaTmp.size();
            for(int i=0 ; i<tamanhoTmp ; i++){
                CRUD.create(arq1, contaTmp.get(0));//Adiciono contas no arquivo 1
                contaTmp.remove(0);//Removo o elemento do arraylist temporario
            }

            for(int i=0 ; i<limite ; i++){
                if(contas.size() > 0){//Verifico se o arraylist não está vazio
                    contaTmp.add(contas.get(0));//Adiciono o elemento no arraylist temporario
                    contas.remove(0);//Removo o elemento do arraylist principal
                }
            }

            Collections.sort(contaTmp);//Ordeno o arraylist temporario

            tamanhoTmp = contaTmp.size();
            for(int i=0 ; i<tamanhoTmp ; i++){
                CRUD.create(arq2, contaTmp.get(0));//Adiciono contas no arquivo 2
                contaTmp.remove(0);//Removo o elemento do arraylist temporario
            }

        }




        //-------------------- LEITURA DOS ARQUIVOS 1 E 2 --------------------
        ArrayList<Conta> conta1 = new ArrayList<Conta>();
        ArrayList<Conta> conta2 = new ArrayList<Conta>();

        try{
            arq1.seek(0);//Ponteiro no inicio do arquivo
            while(arq1.getFilePointer() < arq1.length()){//Percorro todo o arquivo
                Conta novaConta1 = new Conta();
                char lapide = arq1.readChar();//Leio a lapide
                if(lapide == ' '){//Verifico se a lapide esta ativa
                    int tamanhoArq = arq1.readInt();//Armazeno o tamnho do arquivo
                    int idRescue = arq1.readInt();//Seto o id
                    novaConta1 = read0(arq1, idRescue);
                    conta1.add(novaConta1);
                }
                else{
                    arq1.skipBytes(arq1.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
        }catch(Exception e){
            System.out.println("Erro ao ler arquivo !");
        }

        try{
            arq2.seek(0);//Ponteiro no inicio do arquivo
            while(arq2.getFilePointer() < arq2.length()){//Percorro todo o arquivo
                Conta novaConta2 = new Conta();
                if(arq2.readChar() == ' '){//Verifico se a lapide esta ativa
                    int tamanhoArq = arq2.readInt();//Armazeno o tamnho do arquivo
                    int idRescue = arq2.readInt();//Seto o id
                    novaConta2 = read0(arq2, idRescue);
                    conta2.add(novaConta2);
                }
                else{
                    arq2.skipBytes(arq2.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
        }catch(Exception e){
            System.out.println("Erro ao ler arquivo !");
        }




        //-------------------- PRIMEIRA INTERCALAÇÃO --------------------
        for(int j=0 ; j<voltas ; j++){
            int usosConta1 = 0;
            int usosConta2 = 0;
            
            for(int i=0; i<(limite*2) ; i++){
                if(conta1.size() == 0 && conta2.size() == 0){
                }

                else if(conta1.size() != 0 && conta2.size() == 0 && usosConta1 < limite){
                    CRUD.create(arq3, conta1.get(0));
                    conta1.remove(0);
                    usosConta1++;
                }
                else if(conta1.size() == 0 && conta2.size() != 0 && usosConta2 < limite){
                    CRUD.create(arq3, conta2.get(0));
                    conta2.remove(0);
                    usosConta2++;
                } 
                else if(usosConta1 == limite && usosConta2 <limite){
                    CRUD.create(arq3, conta2.get(0));
                        conta2.remove(0);
                        usosConta2++;
                }
                else if(usosConta1 < limite && usosConta2 == limite){
                    CRUD.create(arq3, conta2.get(0));
                        conta2.remove(0);
                        usosConta1++;
                }
                else if(conta1.size() != 0 && conta2.size() != 0){
                    if(conta1.get(0).getIdConta() < conta2.get(0).getIdConta() && usosConta1 < limite){
                        CRUD.create(arq3, conta1.get(0));
                        conta1.remove(0);
                        usosConta1++;
                    }
        
                    else if(conta1.get(0).getIdConta() > conta2.get(0).getIdConta() && usosConta2 < limite){
                        CRUD.create(arq3, conta2.get(0));
                        conta2.remove(0);
                        usosConta2++;
                    }
                }
            }

            usosConta1 = 0;
            usosConta2 = 0;

            for(int i=0; i<10 ; i++){  
                if(conta1.size() == 0 && conta2.size() == 0){
                }

                else if(conta1.size() != 0 && conta2.size() == 0 && usosConta1 < limite){
                    CRUD.create(arq4, conta1.get(0));
                    conta1.remove(0);
                    usosConta1++;
                }
                else if(conta1.size() == 0 && conta2.size() != 0 && usosConta2 < limite){
                    CRUD.create(arq4, conta2.get(0));
                    conta2.remove(0);
                    usosConta2++;
                }
                else if(usosConta1 == limite && usosConta2 <limite){
                    CRUD.create(arq4, conta2.get(0));
                        conta2.remove(0);
                        usosConta2++;
                }
                else if(usosConta1 < limite && usosConta2 == limite){
                    CRUD.create(arq4, conta2.get(0));
                        conta2.remove(0);
                        usosConta1++;
                }
                else if(conta1.size() != 0 && conta2.size() != 0){
                    if(conta1.get(0).getIdConta() < conta2.get(0).getIdConta() && usosConta1 < limite){
                        CRUD.create(arq4, conta1.get(0));
                        conta1.remove(0);
                        usosConta1++;
                    }
        
                    else if(conta1.get(0).getIdConta() > conta2.get(0).getIdConta() && usosConta2 < limite){
                        CRUD.create(arq4, conta2.get(0));
                        conta2.remove(0);
                        usosConta2++;
                    }
                }
            }

        }


        //-------------------- LEITURA DOS ARQUIVOS 3 E 4 --------------------
        arq1.setLength(0);// Limpa o arquivo
        arq2.setLength(0); // Limpa o arquivo
        conta1.clear(); // Limpa o arraylist
        conta2.clear(); // Limpa o arraylist

        try{
            arq3.seek(0);//Ponteiro no inicio do arquivo
            while(arq3.getFilePointer() < arq3.length()){//Percorro todo o arquivo
                Conta novaConta = new Conta();
                char lapide = arq3.readChar();//Leio a lapide
                if(lapide == ' '){//Verifico se a lapide esta ativa
                    int tamanhoArq = arq3.readInt();//Armazeno o tamnho do arquivo
                    int idRescue = arq3.readInt();//Seto o id
                    novaConta = read0(arq3, idRescue);
                    conta1.add(novaConta);
                }
                else{
                    arq3.skipBytes(arq3.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
        }catch(Exception e){
        }

        try{
            arq4.seek(0);//Ponteiro no inicio do arquivo
            while(arq4.getFilePointer() < arq4.length()){//Percorro todo o arquivo
                Conta novaConta = new Conta();
                if(arq4.readChar() == ' '){//Verifico se a lapide esta ativa
                    int tamanhoArq = arq4.readInt();//Armazeno o tamnho do arquivo
                    int idRescue = arq4.readInt();//Seto o id
                    novaConta = read0(arq4, idRescue);
                    conta2.add(novaConta);
                }
                else{
                    arq4.skipBytes(arq4.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
        }catch(Exception e){
        }



        //-------------------- SEGUNDA INTERCALAÇÃO --------------------

        for(int j=0 ; j<voltas ; j++){
            int usosConta1 = 0;
            int usosConta2 = 0;

            for(int i=0; i<(limite*4) ; i++){
                if(conta1.size() == 0 && conta2.size() == 0){
                }

                else if(conta1.size() != 0 && conta2.size() == 0 && usosConta1 < limite*2){
                    CRUD.create(arq1, conta1.get(0));
                    conta1.remove(0);
                    usosConta1++;
                }
                else if(conta1.size() == 0 && conta2.size() != 0 && usosConta2 < limite*2){
                    CRUD.create(arq1, conta2.get(0));
                    conta2.remove(0);
                    usosConta2++;
                }
                else if(usosConta1 == limite && usosConta2 <limite*2){
                    CRUD.create(arq1, conta2.get(0));
                        conta2.remove(0);
                        usosConta2++;
                }
                else if(usosConta1 < limite && usosConta2 == limite*2){
                    CRUD.create(arq1, conta2.get(0));
                        conta2.remove(0);
                        usosConta1++;
                }
                else if(conta1.size() != 0 && conta2.size() != 0){
                    if(conta1.get(0).getIdConta() < conta2.get(0).getIdConta() && usosConta1 < limite*2){
                        CRUD.create(arq1, conta1.get(0));
                        conta1.remove(0);
                        usosConta1++;
                    }
        
                    else if(conta1.get(0).getIdConta() > conta2.get(0).getIdConta() && usosConta2 < limite*2){
                        CRUD.create(arq1, conta2.get(0));
                        conta2.remove(0);
                        usosConta2++;
                    }
                }

            }

            usosConta1 = 0;
            usosConta2 = 0;

            for(int i=0; i<(limite*4) ; i++){  
                if(conta1.size() == 0 && conta2.size() == 0){
                }

                else if(conta1.size() != 0 && conta2.size() == 0 && usosConta1 < limite*2){
                    CRUD.create(arq2, conta1.get(0));
                    conta1.remove(0);
                    usosConta1++;
                }
                else if(conta1.size() == 0 && conta2.size() != 0 && usosConta2 < limite*2){
                    CRUD.create(arq2, conta2.get(0));
                    conta2.remove(0);
                    usosConta2++;
                }
                else if(usosConta1 == limite*2 && usosConta2 <limite*2){
                    CRUD.create(arq2, conta2.get(0));
                        conta2.remove(0);
                        usosConta2++;
                }
                else if(usosConta1 < limite*2 && usosConta2 == limite*2){
                    CRUD.create(arq2, conta2.get(0));
                        conta2.remove(0);
                        usosConta1++;
                }
                else if(conta1.size() != 0 && conta2.size() != 0){
                    if(conta1.get(0).getIdConta() < conta2.get(0).getIdConta() && usosConta1 < limite*2){
                        CRUD.create(arq2, conta1.get(0));
                        conta1.remove(0);
                        usosConta1++;
                    }
        
                    else if(conta1.get(0).getIdConta() > conta2.get(0).getIdConta() && usosConta2 < limite*2){
                        CRUD.create(arq2, conta2.get(0));
                        conta2.remove(0);
                        usosConta2++;
                    }
                }
            }         
        }


        
        //-------------------- LEITURA DOS ARQUIVOS 1 E 2 --------------------
        conta1.clear();
        conta2.clear();

        try{
            arq1.seek(0);//Ponteiro no inicio do arquivo
            while(arq1.getFilePointer() < arq1.length()){//Percorro todo o arquivo
                Conta novaConta1 = new Conta();
                char lapide = arq1.readChar();//Leio a lapide
                if(lapide == ' '){//Verifico se a lapide esta ativa
                    int tamanhoArq = arq1.readInt();//Armazeno o tamnho do arquivo
                    int idRescue = arq1.readInt();//Seto o id
                    novaConta1 = read0(arq1, idRescue);
                    conta1.add(novaConta1);
                }
                else{
                    arq1.skipBytes(arq1.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
        }catch(Exception e){
            System.out.println("Erro ao ler arquivo !");
        }

        try{
            arq2.seek(0);//Ponteiro no inicio do arquivo
            while(arq2.getFilePointer() < arq2.length()){//Percorro todo o arquivo
                Conta novaConta2 = new Conta();
                if(arq2.readChar() == ' '){//Verifico se a lapide esta ativa
                    int tamanhoArq = arq2.readInt();//Armazeno o tamnho do arquivo
                    int idRescue = arq2.readInt();//Seto o id
                    novaConta2 = read0(arq2, idRescue);
                    conta2.add(novaConta2);
                }
                else{
                    arq2.skipBytes(arq2.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
        }catch(Exception e){
            System.out.println("Erro ao ler arquivo !");
        }


        //-------------------- TERCEIRA INTERCALAÇÃO --------------------

        int tamanhoFinal = conta1.size() + conta2.size();
        for(int i=0; i<tamanhoFinal ; i++){
            if(conta1.size() == 0 && conta2.size() == 0){
            }

            else if(conta1.size() != 0 && conta2.size() == 0 ){
                CRUD.create(arqFinal, conta1.get(0));
                conta1.remove(0);
            }
            else if(conta1.size() == 0 && conta2.size() != 0){
                CRUD.create(arqFinal, conta2.get(0));
                conta2.remove(0);
            }
            else if(conta1.size() != 0 && conta2.size() != 0){
                if(conta1.get(0).getIdConta() < conta2.get(0).getIdConta()){
                    CRUD.create(arqFinal, conta1.get(0));
                    conta1.remove(0);
                }
    
                else if(conta1.get(0).getIdConta() > conta2.get(0).getIdConta()){
                    CRUD.create(arqFinal, conta2.get(0));
                    conta2.remove(0);
                }
            }
        }
        
        return true;
    }

}