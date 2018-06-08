package com.eazico.autoreset;

public class add_entry_status {
    int type;//o for new entry 1 for update
    int status;//0 for failure 1 for sucess
    add_entry_status(int type,int status){
        this.status=status;
        this.type=type;
    }
}
