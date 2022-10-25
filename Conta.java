
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;


public class Conta implements Comparable<Conta>{
    private int idConta;
    private String nomePessoa;
    private ArrayList<String> email;
    private String nomeUsuario;
    private String senha;
    private String cpf;
    private String cidade;
    private int transferenciasRealizadas;
    private float saldoConta;


    public Conta(){
        this.idConta = 0;
        this.nomePessoa = "";
        this.email = new ArrayList<String>();
        this.nomeUsuario = "";
        this.senha = "";
        this.cpf = "";
        this.cidade = "";
        this.transferenciasRealizadas = 0;
        this.saldoConta = 0;
    }

    public Conta(int idConta, String nomePessoa, ArrayList<String> email, String nomeUsuario,
        String senha, String cpf, String cidade, int transferenciasRealizadas, float saldoConta){
        this.idConta = idConta;
        this. nomePessoa = nomePessoa;
        this.email = email;
        this.nomeUsuario = nomeUsuario;
        this.senha = senha;
        this.cpf = cpf;
        this.cidade = cidade;
        this.transferenciasRealizadas = transferenciasRealizadas;
        this.saldoConta = saldoConta;
    }

    //idConta
    public void setIdConta(int idConta){
        this.idConta = idConta;
    }
    public int getIdConta(){
        return idConta;
    }

    //Nome Pessoa
    public void setNomePessoa(String nomePessoa){
        this.nomePessoa = nomePessoa;
    }
    public String getNomePessoa(){
        return nomePessoa;
    }

    //Email
    public void setEmail(ArrayList<String> email){
        this.email = email;
    }
    public ArrayList<String> getEmail(){
        return email;
    }

    //Nome Usuário
    public void setNomeUsuario(String nomeUsuario){
        this.nomeUsuario = nomeUsuario;
    }
    public String getNomeUsuario(){
        return nomeUsuario;
    }

    //Senha
    public void setSenha(String senha){
        this.senha = senha;
    }
    public String getSenha(){
        return senha;
    }

    //Cpf
    public void setCpf(String cpf){
        this.cpf = cpf;
    }

    public String getCpf(){
        return cpf;
    }

    //Cidade
    public void setCidade(String cidade){
        this.cidade = cidade;
    }

    public String getCidade(){
        return cidade;
    }

    //Transferências realizadas
    public void setTransferenciasRealizadas(int transferenciasRealizadas){
        this.transferenciasRealizadas = transferenciasRealizadas;
    }
    
    public int getTransferenciasRealizadas(){
        return transferenciasRealizadas;
    }

    //Saldo Conta
    public void setSaldoConta(float saldoConta){
        this.saldoConta = saldoConta;
    }

    public float getSaldoConta(){
        return saldoConta;
    }


    // //Metodo ToString
    // public String toString(){//Representação textualmente do objeto
    //     DecimalFormat df= new DecimalFormat("#,##0.00");
    //     return "\nID:"+ idConta +
    //             "\nNome:"+nomePessoa +
    //             ""
    //             "\nPontos:"+ df.format(pontos);
    // }
    

    public byte[] toByteArray() throws IOException{//Conversão para um array de bytes

        ByteArrayOutputStream baos = new ByteArrayOutputStream();//Cria o array de bytes
        DataOutputStream dos = new DataOutputStream(baos);//Cria um objeto de fluxo de saida de dados.

        //Passagem de todos os parametros para o array de bytes
        dos.writeInt(this.getIdConta());
        dos.writeUTF(this.getNomePessoa());
        dos.writeInt(this.getEmail().size());
        for(int i=0 ; i<this.getEmail().size() ; i++){
            dos.writeUTF(this.getEmail().get(i));
        }
        dos.writeUTF(this.getNomeUsuario());
        dos.writeUTF(this.getSenha());
        dos.writeUTF(this.getCpf());
        dos.writeUTF(this.getCidade());
        dos.writeInt(this.getTransferenciasRealizadas());
        dos.writeFloat(this.getSaldoConta());

        return baos.toByteArray();
    }


    public static int pegarId(RandomAccessFile raf) throws IOException{
        int id = 0;
        try{
            raf.seek(0);

            id = raf.readInt();

        }catch(Exception e){
            raf.seek(0);
            raf.writeInt(0);
        }
        return id;
    }

    public static void atualizarId(RandomAccessFile raf, Conta novaConta) throws IOException{//Operação para atualizar o id total

            raf.seek(0);//Volto para o inicio do arquivo
            raf.writeInt(novaConta.getIdConta());//Atualizo o ultimo id
    }

    @Override
    public int compareTo(Conta i) {
        // TODO Auto-generated method stub
        if (this.idConta > i.getIdConta()){ 
            return 1; 
            } if (this.idConta < i.getIdConta()) { 
            return -1; 
            }
            return 0; 
    }

}

