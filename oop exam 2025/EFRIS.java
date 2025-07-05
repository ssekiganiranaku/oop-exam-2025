abstract class TaxCategory {
    public abstract double calculateVAT(double amount);
}

//subclass retailer
class Retailer extends TaxCategory {
    @Override
    public double calculateVAT(double amount) {
        return amount * 0.18; 
    }
}
//subclass wholesaler
class Wholesaler extends TaxCategory {
    @Override
    public double calculateVAT(double amount) {
        return amount * 0.15; 
    }
}
//subclass importer
class Importer extends TaxCategory {
    @Override
    public double calculateVAT(double amount) {
        return amount * 0.10; 
    }
}


public class EFRISTaxSystem {
    public static void main(String[] args) {
        
        TaxCategory[] taxpayers = new TaxCategory[3];
        taxpayers[0] = new Retailer();
        taxpayers[1] = new Wholesaler();
        taxpayers[2] = new Importer();

        double[] transactionAmounts = {1000.0, 2000.0, 3000.0};

        // heres the runtime polymorphism
        for (int i = 0; i < taxpayers.length; i++) {
            double vat = taxpayers[i].calculateVAT(transactionAmounts[i]);
            System.out.printf("Taxpayer Type %d: VAT on %.2f = %.2f%n",
                    i + 1, transactionAmounts[i], vat);
        }
    }
}