import java.util.*;
public abstract class Bank {
    int pin;
    protected double balance;
    String name;
     List<String>trns=new ArrayList<>();
    Bank(int num){
        pin=num; 
    }
    void name(String cusname){
        name=cusname;
    }
    void dep(double dep){
        balance+=dep;
        System.out.println("Amount deposited.");
        trns.add(dep+" Deposited.");
    }
    abstract void with(double a);
    int l=0;
    List<Double> ln=new ArrayList<>();
    abstract void loan(double a);
    double balance(){
        return balance;
    }
    abstract void PayLoan(int a);
    void transaction(){
        for (String string : trns) {
            System.out.println(string);
        }
    }
}
class currentBank extends Bank{
    currentBank(int pin) {
        super(pin);
    }
    void loan(double loan){
        ln.add(loan); 
        balance+=loan;l++;
        System.out.println("Loan Amount Added to your balance.");
        trns.add(loan+" Loan Added.");
    }
    void PayLoan(int lnpay){
        try{
            if (balance()>ln.get(lnpay-1)) {
                balance-=ln.get(lnpay-1);
                l-=1;
                trns.add(ln.get(lnpay-1)+" Loan Paid");
                ln.remove(lnpay-1);
                System.out.println("Loan paid");
            }
            else{
                System.out.println("insufficient balance.");
            }
        }
         
        catch(IndexOutOfBoundsException e){
            System.out.println("Enter correct loan SNo.");
        }
    }
    void with(double with){
       if(balance>1000 && balance-with>balance){
            balance-=with;
            System.out.println("Amount withdrawed.");
            trns.add(with+" Amount withdrawed.");
        }
        
        else{
            System.out.println("Balance less than 1000 or Negative Balance");
        }
    }
}
class saveBank extends Bank{
    saveBank(int pin){
        super(pin);
    }
    void loan(double loan){
        if(loan<500000){
            ln.add(loan); 
            balance+=loan;l++;
            System.out.println("Loan Amount Added to your balance.");
            trns.add(loan+" Loan Added.");
        }
        else{
            System.out.println("Savings Account not allowed to get loan above 500000");
        }
    }
    void PayLoan(int lnpay){
        try{
            if (balance()>ln.get(lnpay-1)) {
                balance-=ln.get(lnpay-1);
                l-=1;
                trns.add(ln.get(lnpay-1)+" Loan Paid");
                ln.remove(lnpay-1);
                System.out.println("Loan paid");
            }
            else{
                System.out.println("insufficient balance.");
            }
        }
        catch(IndexOutOfBoundsException e){
            System.out.println("Enter correct loan SNo.");
        }
    }
    void with(double with){
        if(balance>500 && balance-with>balance){
            balance-=with;
            System.out.println("Amount withdrawed.");
        }
        else{
            System.out.println("Balance less than ₹500 or Negative Balance");
        }
    }
}
class Main{
    public static void main(String[] args) {
    Scanner muf=new Scanner(System.in);
    Map<Integer,Bank>account=new HashMap<>();
    System.out.println("\nWelcome to Bank Of JAVA");
    while (true) {
        System.out.println("\n1.Add your new account.");
        System.out.println("2.View yor account.");
        System.out.println("3.Remove your account.");
        System.out.println("4.STOP.");
        System.out.println("Enter choice:");
        int choice=muf.nextInt();
        if(choice==1){
            while(true){
                System.out.println("1.Saving Account.");
                System.out.println("2.Current Account.");
                int choice1=muf.nextInt();
                if(choice1==1){
                    System.out.println("Enter your new 4 digit pin.");
                    int pin=muf.nextInt();
                    if(pin<1000||pin>9999){
                        System.out.println("Pin should be in 4 digits!");
                    }
                    else{
                        System.out.println("Enter your Name: ");
                        muf.nextLine();
                        String name=muf.nextLine();
                        account.put(pin,(new saveBank(pin)));
                        account.get(pin).name(name);
                        System.out.println("Congrats your Account created " +account.get(pin).name+ " !!");break;
                    }
                }
                else if(choice1==2){
                    System.out.println("Enter your new 4 digit pin.");
                    int pin=muf.nextInt();
                    if(account.containsKey(pin)){
                        System.out.println("This pin already exist.");
                    }
                    else{
                    if(pin>1000||pin<9999){
                        System.out.println("Enter your Name: ");
                        muf.nextLine();
                        String name=muf.nextLine();
                        account.put(pin,(new currentBank(pin)));
                        account.get(pin).name(name);
                        System.out.println("Congrats your Account created " +account.get(pin).name+ " !!");break;
                        }
                    else{
                        System.out.println("Pin should be in 4 digits!");
                        }   
                    }
                }
                else{
                    System.out.println("Invlid choice");break;
                }
            }
        }
        else if(choice==2){
            System.out.println("Enter your pin: ");
            while (true) {
            int pin=muf.nextInt();
                if(account.containsKey(pin)){
                    System.out.println("Your account founded "+account.get(pin).name );        
                    while (true) {
                        System.out.println("\n1.Deposite & Withdraw.");
                        System.out.println("2.Check balance.");
                        System.out.println("3.Money Transfer.");
                        System.out.println("4.Loan");
                        System.out.println("5.Transaction Historty");
                        System.out.println("6.Back.");
                        System.out.print("Enter choice: ");
                        int choice2=muf.nextInt();    
                        if (choice2==1) {
                            System.out.print("Enter your pin: ");
                            while (true) {
                                int pin2=muf.nextInt();
                                if(pin2==pin){
                                    while (true) {
                                        System.out.println("\n1.Deposite.");
                                        System.out.println("2.Withdraw.");
                                        System.out.println("3.Back.");
                                        System.out.print("Enter choice: ");
                                        int choice3=muf.nextInt();     
                                        if(choice3==1){
                                                System.out.println("Enter Deposite amount: ");
                                                int depamt=muf.nextInt();
                                                account.get(pin2).dep(depamt);
                                        }               
                                        else if(choice3==2){
                                                System.out.println("Enter withdraw amount: ");
                                                int withamt=muf.nextInt();
                                            account.get(pin2).with(withamt);
                                        }        
                                        else if(choice3==3){
                                            break;
                                        }
                                        else{
                                            System.out.println("Invalid choice.");
                                        }
                                    }break;
                                }
                                else{
                                    System.out.println("Wrong pin.");
                                    System.out.println("Enter pin Again!");
                                }
                            }
                        }
                        else if(choice2==2){
                            System.err.println("Enter pin: ");
                            while (true) {   
                                int pin3=muf.nextInt();
                                if(pin3==pin){
                                    System.out.println("Balance Amount:"+account.get(pin3).balance());
                                    if(account.get(pin).l!=0){
                                        System.err.println("You have "+account.get(pin).l+" loan");
                                    }
                                    break;
                                }
                                else{
                                    System.out.println("Wrong pin.");
                                    System.out.println("Enter pin Again!");
                                }
                            }
                        }
                        else if(choice2==3){
                            while (true) {
                            System.out.println("Enter Pin.");
                            int cpin=muf.nextInt();
                                if (cpin==pin) {
                                    System.out.println("Enter Money Transfer Account Pin.");
                                    int tpin=muf.nextInt();
                                    if(tpin!=pin){
                                        if (account.containsKey(tpin)) {
                                            System.out.println("Enter Transfer Amount.");
                                            double tamt=muf.nextInt();
                                            if (account.get(pin).balance()>=tamt) {
                                                account.get(pin).balance-=tamt;
                                                account.get(pin).trns.add("Amount "+tamt+" Sended to Account N/O:"+tpin);
                                                account.get(tpin).balance+=tamt;
                                                account.get(tpin).trns.add("Amount "+tamt+" Recived from Account N/O:"+pin);
                                                System.out.println("Amount Transfered Succesfully."); break;
                                            }
                                            else {
                                                System.out.println("Insufficient Balance.");break;
                                            }
                                        }
                                        else{
                                            System.out.println("No Account found.");break;
                                        }
                                    }
                                    else{
                                        System.out.println("Enter correct pin.");
                                    }
                                }
                                else{
                                    System.out.println("Wrong pin.");
                                    System.out.println("Enter pin Again.");break;
                                }
                            }
                        }
                        else if(choice2==4){
                            System.out.println("Enter pin.");
                            int cpin=muf.nextInt();
                            if(cpin==pin){
                                while (true) {
                                    System.out.println("1.Request Loan.");
                                    System.out.println("2.Pay Loan.");
                                    int choice3=muf.nextInt();
                                    if(choice3==1){
                                        System.out.println("Enter your salary.");
                                        int sal=muf.nextInt();
                                        if (sal>50000 && account.get(pin).balance()>10000) {
                                            System.out.println("Enter loan Amount.");
                                            double loan=muf.nextInt();
                                            account.get(pin).loan(loan);break;
                                        }
                                        else {
                                            System.out.println("No Loan!");break;
                                        }
                                    }
                                    else if (choice3==2) {
                                       if (account.get(cpin).l>0) {
                                            int sno=1;
                                            for (double i : account.get(cpin).ln) {
                                                System.out.println("Loan No:"+sno+++" Loan Amount:"+i);
                                            }
                                            System.out.println("you have "+account.get(cpin).ln.size()+" loans");
                                            System.out.println("Enter the Loan No you want to pay");
                                            int lnpay=muf.nextInt();
                                            account.get(cpin).PayLoan(lnpay);break;
                                        }
                                        else{
                                            System.out.println("You have no loans.");break;
                                        }
                                    }
                                }
                            }
                            else{
                                System.out.println("Enter corret pin");
                            }
                        }
                        else if(choice2==5){
                            account.get(pin).transaction();
                        }
                        else if(choice2==6){
                                break;
                        }
                        else{
                            System.out.println("Invalid choice.");
                        }
                    } break;  
                }
                else{
                    System.out.println("Account not Found.");
                    break;
                }
            }
        }
        else if(choice==3){
                while(true){ 
                    System.out.println("Enter 0 to Exit.");
                    System.out.println("Enter your pin.");
                    int pin=muf.nextInt();
                    if(account.containsKey(pin)){
                        if(account.get(pin).balance()==0){
                            System.out.println("Enter your name.");
                            muf.nextLine();
                            String name=muf.nextLine();
                            if(account.get(pin).name.equals(name)){
                                System.out.println("Your account removed "+account.get(pin).name);
                                account.remove(pin);break;
                            }
                            else{
                                System.out.println("Enter correct Name.");
                            }
                        }
                        else{
                            System.out.println("Withdraw total Amount from your Account.");
                        }
                    }
                    else if(pin==0)break;
                    else{
                        System.out.println("Account not found");
                    }
                }
            }
            else if(choice==4){
                 System.out.println("Thanks for choocing Bank of JAVA"); 
                 break;
            }
            else{
                System.out.println("Invalid choice.");
            }
        }           
    }
}