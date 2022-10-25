import java.util.ArrayList;
import java.io.IOException;
import java.io.RandomAccessFile;


public class CRUD{

    //---------------------------- METODO CREATE ----------------------------

    public static boolean create(RandomAccessFile raf, Conta novaConta){

        try{
            raf.seek(raf.length());//Posiciono o ponteiro no final do arquivo
            raf.writeChar(' ');//Criação da lápide
            byte[]array = novaConta.toByteArray();
            raf.writeInt(array.length);//Tamanho do registro
            raf.write(array);//Escrever os dados do toByteArray
            
            return true;
        }catch(Exception e){

            return false;
        }    
    }



    

    //---------------------------- METODO READ ----------------------------
    public static Conta read(RandomAccessFile raf, int idPesquisado){
        
        try{
            Conta novaConta = new Conta();

            raf.seek(4);//Ponteiro no inicio do arquivo
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


    //---------------------------- METODO UPDATE ----------------------------
    public static Boolean update(RandomAccessFile raf, Conta contaAtualizar, int idAtualizacao) throws IOException{
        
        int tamanho1 = 0, tamanho2 = 0;
        tamanho1 = contaAtualizar.toByteArray().length;

        try{
            Conta novaConta = new Conta();

            raf.seek(4);//Ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()){//Percorro todo o arquivo
                char lapide4 = raf.readChar();
                if(lapide4 == ' '){//Verifico se a lapide esta ativa
                    tamanho2 = raf.readInt();//Armazeno o tamanho do arquivo
                    novaConta.setIdConta(raf.readInt());//Seto o id da conta para verificar
                    if(novaConta.getIdConta() == idAtualizacao){//Consulta para verificar se o id da primeira conta é o desejado
                        if(tamanho1 <= tamanho2){
                            raf.writeUTF(contaAtualizar.getNomePessoa());//Escrevo o nome
                            raf.writeInt(contaAtualizar.getEmail().size());//Escrevo a quantidade de email
                            for(int i=0 ; i<contaAtualizar.getEmail().size() ; i++){
                                raf.writeUTF(contaAtualizar.getEmail().get(i));
                            }
                            raf.writeUTF(contaAtualizar.getNomeUsuario());//Escrevo o nome de Usuario
                            raf.writeUTF(contaAtualizar.getSenha());//Escrevo a senha
                            raf.writeUTF(contaAtualizar.getCpf());//Escrevo o cpf
                            raf.writeUTF(contaAtualizar.getCidade());//Escrevo a Cidade
                            raf.writeInt(contaAtualizar.getTransferenciasRealizadas());//Escrevo as qtd de transferencias
                            raf.writeFloat(contaAtualizar.getSaldoConta());//Escrevo o saldo da conta
                            return true;
                        }
                        else{
                            raf.seek(raf.getFilePointer() - 10);//Volto o ponteiro para o inicio do registro
                            raf.writeChar('*');//Lapide ativa

                            return create(raf, contaAtualizar);
                        }
                    }
                    else{//Se o id da conta não for o requerido
                        raf.skipBytes(tamanho2 - 4);//Pulo o arquivo inteiro
                    }
                }
                else{
                    raf.skipBytes(raf.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
            return null;
            }catch(Exception e){
            System.out.println("Falha ao atualizar o registro...");
            return null;
        }
    }

    //---------------------------- METODO DELETE ----------------------------
    public static Boolean delete(RandomAccessFile raf, int idDeletado) throws IOException{
        
        int tamanhoDado;

        try{
            Conta novaConta = new Conta();

            raf.seek(4);//Ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()){//Percorro todo o arquivo
                char lapide = raf.readChar(); 
                if(lapide == ' '){//Verifico se a lapide esta ativa
                    tamanhoDado = raf.readInt();//Armazeno o tamanho do arquivo
                    novaConta.setIdConta(raf.readInt());//Seto o id da conta para verificar
                    if(novaConta.getIdConta() == idDeletado){//Consulta para verificar se o id da primeira conta é o desejado
                        raf.seek(raf.getFilePointer() - 10);//Volto o ponteiro para o inicio do registro
                            raf.writeChar('*');//Ativo a Lápide
                            return true;
                    }
                    else{//Se o id da conta não for o requerido
                        raf.skipBytes(tamanhoDado - 4);//Pulo o Registro inteiro
                    }
                }
                else{
                    raf.skipBytes(raf.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
            return false;
            }catch(Exception e){
            System.out.println("Falha ao Deletar o registro...");
            return false;
        }
    }


    
    


    public static boolean pesquisaNomeUsuario(RandomAccessFile raf, String pesquisa){
       
        try{
            Conta novaConta = new Conta();
            String nomepesquisado = "";

            raf.seek(4);//Ponteiro no inicio do arquivo
            while(raf.getFilePointer() < raf.length()){//Percorro todo o arquivo
                char lapide = raf.readChar();
                if(lapide == ' '){//Verifico se a lapide esta ativa
                    raf.readInt();//Leio o tamnho do registro
                    novaConta.setIdConta(raf.readInt());//Seto o id da conta para verificar 
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

                    nomepesquisado = novaConta.getNomeUsuario();
                    if(nomepesquisado.compareTo(pesquisa) == 0){
                        return true;
                    }  
                }
                else{
                    raf.skipBytes(raf.readInt());//Pulo o registro ja que a lápide vai estar desativada
                }
            }
            return false;
            }catch(Exception e){;
            return false;
        }
    }
}





