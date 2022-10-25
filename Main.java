import java.util.ArrayList;
import java.util.Scanner;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.io.IOException;


//----------------------- COISAS A RESOLVER ----------------------

//Verificar se o cpf é valido



public class Main{

    public static void main(String[] args) throws IOException{

        Scanner sc = new Scanner(System.in);
        RandomAccessFile raf = new RandomAccessFile("dados.dat", "rw");//Crio o arquivo 4

        int opcao = 0;
        char continuar = 's';

        //---------------- Crianção das contas --------------------

        try{
            do{
                System.out.println("------------- Menu -------------");
                System.out.println("\nDigite a opção desejada:");
                System.out.println("1 - Criar conta  \n2 - Realizar transferencia \n3 - Ler Registro (id) \n4 - Atualizar registro \n5 - Deletar registro \n6 - Ordenar Registro\n");     
            
                do{
                    opcao = sc.nextInt();
                    
                    if(opcao < 1 || opcao > 6){
                        System.out.println("Digite uma opção valida !");
                    }

                }while(opcao < 1 && opcao > 6);



                //---------------- Realizando a operação escolhida ----------------

                //---------------- Opção 1: Criar conta ----------------
                switch(opcao){
                    case 1:
                    System.out.println("\n\n\n -------------- Criação da Conta -------------- ");
                    System.out.println("\n\nVamos começar a criar sua conta ! ");

                    Conta novaConta = new Conta();//Crio o objeto da conta
                    
                    //ID da Conta
                    novaConta.setIdConta(novaConta.pegarId(raf) + 1);
                    System.out.println("\nSeu ID é: " + novaConta.getIdConta());

                    //Nome da Conta
                    System.out.println("\nDigite o nome: \n");
                    novaConta.setNomePessoa(sc.next());

                    //Email(s) da Conta
                    ArrayList<String> arrayTmp = new ArrayList<String>();
                    String input = "s";
                    do{
                        System.out.println("\nDigite seu Email : ");
                        arrayTmp.add(sc.next());

                        System.out.println("Deseja adicionar mais emails ?  s/n ");
                        input = sc.next();
                    }while(input.equals("s") || input.equals("S"));

                    novaConta.setEmail(arrayTmp);
                

                    //nomeUsuario da conta
                    int usuarioInvalido = 0;
                    do{
                        usuarioInvalido = 0;
                        String tmp = "";
                        System.out.println("\nDigite o nome de Usuario da conta:");
                        tmp = sc.next();

                        if(CRUD.pesquisaNomeUsuario(raf, tmp) == true){
                            System.out.println("Nome de Usuário ja em uso, por favor digite outro");
                            usuarioInvalido++;
                        }
                        else{
                            novaConta.setNomeUsuario(tmp);
                        }

                    }while(usuarioInvalido != 0);

                    //Senha da Conta
                    System.out.println("\nDigite a senha:");
                    novaConta.setSenha(sc.next());

                    //Cpf da conta
                    System.out.println("\nDigite o cpf:");
                    novaConta.setCpf(sc.next());

                    //Cidade
                    System.out.println("\nDigite a cidade:");
                    novaConta.setCidade(sc.next());

                    //transferencias ralizadas
                    novaConta.setTransferenciasRealizadas(0);

                    //Saldo da Conta
                    System.out.println("\nDigite o saldo da conta:");
                    novaConta.setSaldoConta(sc.nextFloat());

                    if(CRUD.create(raf, novaConta) == true){
                        Conta.atualizarId(raf, novaConta);
                        System.out.println("\n\nConta cadastrada !");
                    }
                    else {
                        System.out.println("Erro ao criar conta !");
                    }
                    break;


                    //---------------- Opção 2: Transferência  ----------------
                    case 2:
                    Conta conta1 = new Conta();
                    Conta conta2 = new Conta();
                    int idTransferencia1 = 0, idTransferencia2 = 0; 
                    float valorTransferencia = 0;

                    System.out.println("\n\n-------------- Transferência de Bancaria --------------");
                    
                    //Pego os nomes de usuarios para que seja feito a transferencia
                    do{
                        System.out.println("\nDigite o id de usuario da conta que você deseja creditar o valor: ");
                        idTransferencia1 = sc.nextInt();
                        conta1 = CRUD.read(raf, idTransferencia1);

                        if(conta1 == null){
                            System.out.println("A conta de id " + idTransferencia1 + " não existe");
                        }
                    }while(conta1 == null);

                    do{
                        System.out.println("\nDigite o id de usuario da conta que você deseja debitar o valor: ");
                        idTransferencia2 = sc.nextInt();
                        conta2 = CRUD.read(raf, idTransferencia2);

                        if(conta1 == null){
                            System.out.println("A conta de id " + idTransferencia2 + " não existe");
                        }
                    }while(conta2 == null);
                        
                    System.out.println("Qual é o valor da transferencia desejada ? ");
                    valorTransferencia = sc.nextFloat();


                    //Inicio a procura dos nomes escolhidos para fazer as devidas mudanças
                    conta1.setSaldoConta(conta1.getSaldoConta() - valorTransferencia);
                    conta2.setSaldoConta(conta2.getSaldoConta() + valorTransferencia);

                    CRUD.update(raf, conta1, idTransferencia1);
                    CRUD.update(raf, conta2, idTransferencia2);
                    System.out.println("\n\nTransferencia realizada com sucesso !!");
                    
                    break;


                    //---------------- Opção 3: Leitura de um registro por ID ----------------
                    case 3:
                    int idDigitado = 0;
                    Conta novaConta2 = new Conta();

                    System.out.println("\n\n-------------- Registro de Contas --------------");
                    System.out.println("\nDigite o ID que você deseja consultar: ");
                    idDigitado = sc.nextInt();
                    
                    novaConta2 = CRUD.read(raf, idDigitado);
                    
                    if(novaConta2 != null){
                        System.out.println("\n\n-------------- Dados da Conta de ID " + idDigitado + " --------------");
                        System.out.println("\nNome: " + novaConta2.getNomePessoa());
                        System.out.println("\nEmail: " + novaConta2.getEmail());
                        System.out.println("\nNome de Usuario: " + novaConta2.getNomeUsuario());
                        System.out.println("\nSenha: " + novaConta2.getSenha());
                        System.out.println("\nCpf: " + novaConta2.getCpf());
                        System.out.println("\nCidade: " + novaConta2.getCidade());
                        System.out.println("\nTransferencias realizadas: " + novaConta2.getTransferenciasRealizadas());
                        System.out.println("\nSaldo da Conta: " + novaConta2.getSaldoConta());
                    }
                    else{
                        System.out.println("Conta não encontrada !");
                    }
                    break;


                    case 4:
                    int idAtualizacao = 0;
                    Conta novaConta3 = new Conta();
                    int valorAtualizacao = 0;
                    String continuarAtualizacao = "s";

                    System.out.println("\n\n-------------- Arualização de contas --------------");
                    System.out.println("\nDigite o id da conta em que você deseja atualizar :");
                    idAtualizacao = sc.nextInt();
                    novaConta3 = CRUD.read(raf, idAtualizacao);

                    if(novaConta3 == null){
                        System.out.println("A conta digitada não foi encontrada !");
                    }
                    else{
                        do{
                            System.out.println("\n-------------- Arualização dos dados: --------------");
                            System.out.println("Digite o valor desejado: ");
                            System.out.println("1 - Nome\n2 - Email\n3 - Nome de Usuario\n4 - Senha\n5 - Cpf\n6 - Cidade\n7 - Saldo da Conta\n8 - Cancelar");
                            valorAtualizacao = sc.nextInt();
                            
                            switch(valorAtualizacao){
                                case 1:
                                System.out.println("\nDigite o novo nome:");
                                novaConta3.setNomePessoa(sc.next());
                                break;

                                case 2:
                                ArrayList<String> arrayTmpAtualizacao = new ArrayList<String>();
                                String input2 = "s";
                                do{
                                    System.out.println("\nDigite o novo Email : ");
                                    arrayTmpAtualizacao.add(sc.next());
            
                                    System.out.println("Deseja adicionar mais emails ?  s/n ");
                                    input2 = sc.next();
                                }while(input2.equals("s") || input2.equals("S"));
                                novaConta3.setEmail(arrayTmpAtualizacao);
                                break;

                                case 3:
                                System.out.println("\nDigite o novo nome de Usuario");
                                novaConta3.setNomeUsuario(sc.next());
                                break;

                                case 4:
                                System.out.println("\nDigite a nova senha:");
                                novaConta3.setSenha(sc.next());
                                break;

                                case 5:
                                System.out.println("\nDigite o novo cpf:");
                                novaConta3.setCpf(sc.next());
                                break;

                                case 6:
                                System.out.println("\nDigite a nova cidade:");
                                novaConta3.setCidade(sc.next());
                                break;

                                case 7:
                                System.out.println("\nDigite o novo saldo da conta:");
                                novaConta3.setSaldoConta(sc.nextFloat());
                                break;

                                case 8:
                                System.out.println("Operação Cancelada !");
                                break;
                            }

                            System.out.println("Deseja fazer outra mudança ? (s/n)");
                            continuarAtualizacao = sc.next();

                        }while(continuarAtualizacao == "S" || continuarAtualizacao == "s");

                        CRUD.update(raf, novaConta3, idAtualizacao);
                    }
                    break;


                    case 5:
                    int idDeletado;
                    char confirmarDelete = 's';

                    System.out.println("\n-------------- Remoção dos dados: --------------");
                    do{
                        System.out.println("\nDigite o id que você deseja excluir: ");
                        idDeletado = sc.nextInt();

                        System.out.println("O id " + idDeletado + " esta prestes a ser excluido, deseja continuar ? (s/n)");
                        confirmarDelete = sc.next().charAt(0);
                        
                        if(confirmarDelete == 's' || confirmarDelete == 'S'){
                            CRUD.delete(raf, idDeletado);
                            System.out.println("\nDado deletado com sucesso !");
                        }
                    }while(confirmarDelete == 'N' || confirmarDelete == 'n');
                    break;


                    case 6:
                    Intercalar.intercalacao(raf, 5);
                    break;
                }

                

                    
                do{
                    //Verificar se o usuário deseja fazer outra operação
                    System.out.println("\n\nDeseja fazer outra operação ?");
                    System.out.println("S - Sim \nN - Não");
                    continuar = sc.next().charAt(0);

                    if(continuar != 's' && continuar != 'S' && continuar != 'n' && continuar != 'N'){
                        System.out.println("Digite uma opção Válida !");
                    }

                }while(continuar != 's' && continuar != 'S' && continuar != 'n' && continuar != 'N');
                
            }while(continuar == 's' || continuar == 'S');

        }catch(Exception e){
            System.out.println("Falha ao fazer a operação");
        }
    }

}
