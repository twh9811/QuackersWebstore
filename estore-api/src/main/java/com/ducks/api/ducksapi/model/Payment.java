package com.ducks.api.ducksapi.model;

public abstract class Payment {
    private String name;
    private long card;
    private String expDate;
    private int cvv;
    
    public Payment(String name, long card, String expDate, int cvv){
        this.name = name;
        this.card = card;
        this.expDate = expDate;
        this.cvv = cvv;
    }

    /**
     * @return the name on the card
     */
    public String getName(){
        return this.name;
    }
    /**
     * @return the credit card number
     */
    public long getCard(){
        return this.card;
    }
    /**
     * @return the expiration date on the card
     */
    public String geExpDate(){
        return this.expDate;
    }
    /**
     * @return the CVV code 
     */
    public int getCVV(){
        return this.cvv;
    }
}