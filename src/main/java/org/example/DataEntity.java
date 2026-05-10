package org.example;

import jakarta.persistence.*;

@Entity
@Table(name = "data_entries")
public class DataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;

    private String businessName;

    private String businessType;
    private String gstPan;
    private String industry;
    private String yearsInBusiness;
    private String annualRevenue;
    private String monthlyExpenses;
    private String profitEstimate;
    private String existingEmi;

    private String requestedAmount;

    private String purpose;
    private String tenure;
    private String bankStatements;
    private String gstReturns;
    private String itrFiles;
    private String balanceSheetFiles;
    private String loanDocuments;

    public Long getId() { return id; }
    public String getOwner() { return owner; }
    public void setOwner(String owner) { this.owner = owner; }
    public String getBusinessName() { return businessName; }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public String getBusinessType() { return businessType; }
    public void setBusinessType(String businessType) { this.businessType = businessType; }
    public String getGstPan() { return gstPan; }
    public void setGstPan(String gstPan) { this.gstPan = gstPan; }
    public String getIndustry() { return industry; }
    public void setIndustry(String industry) { this.industry = industry; }
    public String getYearsInBusiness() { return yearsInBusiness; }
    public void setYearsInBusiness(String yearsInBusiness) { this.yearsInBusiness = yearsInBusiness; }
    public String getAnnualRevenue() { return annualRevenue; }
    public void setAnnualRevenue(String annualRevenue) { this.annualRevenue = annualRevenue; }
    public String getMonthlyExpenses() { return monthlyExpenses; }
    public void setMonthlyExpenses(String monthlyExpenses) { this.monthlyExpenses = monthlyExpenses; }
    public String getProfitEstimate() { return profitEstimate; }
    public void setProfitEstimate(String profitEstimate) { this.profitEstimate = profitEstimate; }
    public String getExistingEmi() { return existingEmi; }
    public void setExistingEmi(String existingEmi) { this.existingEmi = existingEmi; }
    public String getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(String requestedAmount) { this.requestedAmount = requestedAmount; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public String getTenure() { return tenure; }
    public void setTenure(String tenure) { this.tenure = tenure; }
    public String getBankStatements() { return bankStatements; }
    public void setBankStatements(String bankStatements) { this.bankStatements = bankStatements; }
    public String getGstReturns() { return gstReturns; }
    public void setGstReturns(String gstReturns) { this.gstReturns = gstReturns; }
    public String getItrFiles() { return itrFiles; }
    public void setItrFiles(String itrFiles) { this.itrFiles = itrFiles; }
    public String getBalanceSheetFiles() { return balanceSheetFiles; }
    public void setBalanceSheetFiles(String balanceSheetFiles) { this.balanceSheetFiles = balanceSheetFiles; }
    public String getLoanDocuments() { return loanDocuments; }
    public void setLoanDocuments(String loanDocuments) { this.loanDocuments = loanDocuments; }
}
