package Exeptions;

public class AppExeption  extends  RuntimeException{
    public AppExeption(String messege){
        super(messege);
    }
    public AppExeption(String messege, Throwable cause){
        super(messege, cause);
    }
}
