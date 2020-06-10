package ba.unsa.etf.rma.rma20siljakamina96.account;

public interface IAccountView {
    void setLimits(double totalLimit, double monthLimit);
    void setBudget(String budget);

    void uploadToServis();

    void setOfflineText(String text);
}
