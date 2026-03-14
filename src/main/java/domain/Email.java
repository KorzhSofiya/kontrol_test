package domain;

import Exeptions.ValidationExeption;

public record Email(String str){
    public Email {
        if(str == null || !str.contains("@")) {
            throw new ValidationExeption("Invalid email format");
        }
    }
}
