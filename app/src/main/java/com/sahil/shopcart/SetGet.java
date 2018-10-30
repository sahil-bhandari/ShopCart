package com.sahil.shopcart;

/**
 * Created by ACER_SAHIL on 09-02-2017.
 */
import com.google.firebase.firestore.IgnoreExtraProperties;

@IgnoreExtraProperties
public class SetGet {

    String id;
    String mail;
    String name;
    String Descrip;
    String image;
    int num = 0;
    private boolean upSelected,downSelected;

    public SetGet(String id, String name, String mail, String image,int num, String Descrip){
        this.id = id;
        this.name = name;
        this.mail = mail;
        this.image = image;
        this.num = num;
    }

    public SetGet(){}

    public String getId(){
        return id;
    }

    public void setNum(int num)
    {
        this.num = num;
    }

    public int getNum()
    {
        return num;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDescrip() {
        return Descrip;
    }

    public void setDescrip(String Descrip) {
        this.Descrip = Descrip;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    }
