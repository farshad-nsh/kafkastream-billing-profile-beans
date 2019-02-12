package domain;


/**
 * @author farshad noravesh
 * @version 1.0.0
 */
public class Billing {

    private String customerId;
    private double billingValue;

    public Billing(){
    }

    public Billing findBillingValue(String customerId){
        if (customerId.equals("farshad_no")){
             billingValue=23.43;
        }
        return this;
    }

    public double getBillingValue() {
        return billingValue;
    }

    public Billing builder(){
        return this;
    }
}
