public class TransactionRecord {
    
    private String buyerTIN;
    private String sellerTIN;
    private double invoiceAmount;
    private String transactionTimestamp;

    // Getter and setter methods
    public String getBuyerTIN() {
        return buyerTIN;
    }

    public void setBuyerTIN(String buyerTIN) {
        this.buyerTIN = buyerTIN;
    }

    public String getSellerTIN() {
        return sellerTIN;
    }

    public void setSellerTIN(String sellerTIN) {
        this.sellerTIN = sellerTIN;
    }

    public double getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(double invoiceAmount) {
        if (invoiceAmount >= 0) {
            this.invoiceAmount = invoiceAmount;
        } else {
            System.out.println("Invalid invoice amount.");
        }
    }

    public String getTransactionTimestamp() {
        return transactionTimestamp;
    }

    public void setTransactionTimestamp(String transactionTimestamp) {
        this.transactionTimestamp = transactionTimestamp;
    }
}