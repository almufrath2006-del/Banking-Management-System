import java.sql.*;
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
    abstract void AddBalance(double balance);
    void dep(double dep,int pin){
        balance+=dep;
        System.out.println("Amount deposited.");
        trns.add(dep+" Deposited.");
        try{
        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
        Statement st=con.createStatement();
        int rs1=st.executeUpdate("update saving set balance=balance+"+dep+" where pin="+pin+";");
        if (rs1==0) {
            int rs2=st.executeUpdate("update current set balance=balance+"+dep+" where pin="+pin+";");            
        }
        int rs=st.executeUpdate("insert transaction_details value("+pin+",'"+dep+" Amount Deposited');");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    abstract void with(double a, int b);
    List<Double> ln=new ArrayList<>();
    abstract void loan(double a); 
    void PayLoan(int lnpay,int pin){
        try{
            if (balance()>ln.get(lnpay-1)) {
                balance-=ln.get(lnpay-1);
                trns.add(ln.get(lnpay-1)+" Loan Paid");
                System.out.println("Loan paid");
                try{
                    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
                    Statement st=con.createStatement();
                    int r=st.executeUpdate("update current set balance=balance-"+ln.get(lnpay-1)+" where pin="+pin+";");
                    if (r==0) {
                        int r1=st.executeUpdate("update saving set balance=balance-"+ln.get(lnpay-1)+" where pin="+pin+";");
                    }
                    int r1=st.executeUpdate("delete from loan where amount="+ln.get(lnpay-1)+";");
                    int r2=st.executeUpdate("insert transaction_details value("+pin+",'"+ln.get(lnpay-1)+" Loan Paid');");
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                ln.remove(lnpay-1);

            }
            else{
                System.out.println("insufficient balance.");
            }
        }
         
        catch(IndexOutOfBoundsException e){
            System.out.println("Enter correct loan SNo.");
        }
    }
    double balance(){
        return balance;
    }
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
    void AddBalance(double balance){
        this.balance=balance;
    }
   
    void loan(double loan){
        ln.add(loan); 
        balance+=loan;
        System.out.println("Loan Amount Added to your balance.");
        trns.add(loan+" Loan Added.");
        try {
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
            Statement st=con.createStatement();
            int rs=st.executeUpdate("insert loan value("+pin+","+loan+");");
            int rs1=st.executeUpdate("update current set balance=balance+"+loan+";");
            int rs2=st.executeUpdate("insert transaction_details value("+pin+",'"+loan+" Loan Added');");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void with(double with,int pin){
       if(balance>1000 && (balance-with)>0){
            balance-=with;
            System.out.println("Amount withdrawed.");
            trns.add(with+" Amount withdrawed.");
            try{
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
                Statement st=con.createStatement();
                int rs1=st.executeUpdate("update current set balance=balance-"+with+" where pin="+pin+";");
                int rs=st.executeUpdate("insert transaction_details value("+pin+",'"+with+" Amount Withdrawed');");
            }
            catch(Exception e){
                e.printStackTrace();
            }
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
    void AddBalance(double balance){
        this.balance=balance;
    }
    void loan(double loan){
        if(loan<500000){
            ln.add(loan); 
            balance+=loan;
            System.out.println("Loan Amount Added to your balance.");
            trns.add(loan+" Loan Added.");
            try {
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
                Statement st=con.createStatement();
                int rs=st.executeUpdate("insert loan value("+pin+",'"+loan+"');");
                int rs1=st.executeUpdate("insert transaction_details value("+pin+",'"+loan+" Loan Added');");
                int rs2=st.executeUpdate("update saving set balance=balance+"+loan+";");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Savings Account not allowed to get loan above 500000");
        }
    }
    void with(double with,int pin){
        if(balance>500 && (balance-with)>0){
            balance-=with;
            System.out.println("Amount withdrawed.");
            trns.add(with+" Amount withdrawed.");
            try{
                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
                Statement st=con.createStatement();
                int rs1=st.executeUpdate("update saving set balance=balance-"+with+" where pin="+pin+";");
                int rs=st.executeUpdate("insert transaction_details value("+pin+",'"+with+" Amount Withdrawed');");
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        else{
            System.out.println("Balance less than 500 or Negative Balance");
        }
    }
}
class Main{
    public static void main(String[] args) throws Exception{
        Map<Integer,Bank>account=new HashMap<>();
        Scanner muf=new Scanner(System.in);
        try{
            Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("select * from saving;");
            while (rs.next()) {
                account.put(rs.getInt(1),(new saveBank(rs.getInt(1))));
                account.get(rs.getInt(1)).name(rs.getString(2));
                account.get(rs.getInt(1)).AddBalance(rs.getDouble(3));
            }
            ResultSet rs1=st.executeQuery("select * from current;");
            while (rs1.next()) {
                account.put(rs1.getInt(1),(new currentBank(rs1.getInt(1))));
                account.get(rs1.getInt(1)).name(rs1.getString(2));
                account.get(rs1.getInt(1)).AddBalance(rs1.getDouble(3));
            }
            ResultSet rs2=st.executeQuery("select * from transaction_details;");
            while (rs2.next()) {
                if (account.containsKey(rs2.getInt(1))) {
                    account.get(rs2.getInt(1)).trns.add(rs2.getString(2));
                }
            }
            ResultSet rs3=st.executeQuery("select * from loan;");
            while (rs3.next()) {
                if (account.containsKey(rs3.getInt(1))) {
                    account.get(rs3.getInt(1)).ln.add(rs3.getDouble(2));
                }
            }
        }
        catch(Exception E){
            E.printStackTrace();
        }
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
                            try{
                                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
                                Statement st=con.createStatement();
                                int rs=st.executeUpdate("insert saving value("+pin+",'"+name+"',"+0+");");
                            }
                            catch(Exception e){
                                e.printStackTrace();
                            }
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
                                try{
                                    Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
                                    Statement st=con.createStatement();
                                    int rs=st.executeUpdate("insert current value("+pin+",'"+name+"',"+0+");");
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
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
                                                account.get(pin2).dep(depamt,pin2);
                                            }               
                                            else if(choice3==2){
                                                System.out.println("Enter withdraw amount: ");
                                                int withamt=muf.nextInt();
                                                account.get(pin2).with(withamt,pin2);
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
                                        if(account.get(pin).ln.size()!=0){
                                            System.err.println("You have "+account.get(pin).ln.size()+" loan");
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
                                        System.out.println("Enter Transfer Account Pin.");
                                        int tpin=muf.nextInt();
                                        if(tpin!=pin){
                                            if (account.containsKey(tpin)) {
                                                System.out.println("Enter Transfer Amount.");
                                                double tamt=muf.nextInt();
                                                if (account.get(pin).balance()>=tamt) {
                                                    try{
                                                        Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
                                                        Statement st=con.createStatement();
                                                        account.get(pin).balance-=tamt;
                                                        int rs=st.executeUpdate("update saving set balance=balance-"+tamt+" where pin="+pin+";");
                                                        if (rs==0) {
                                                            int rs2=st.executeUpdate("update current set balance=balance-"+tamt+" where pin="+pin+";");   
                                                        }
                                                        account.get(pin).trns.add("Amount "+tamt+" Sended to Account N/O:"+tpin);
                                                        int rs1=st.executeUpdate("insert transaction_details value("+pin+",'Amount "+tamt+" Sended to Account N/O:"+tpin+"');");
                                                        account.get(tpin).balance+=tamt;
                                                        int rs2=st.executeUpdate("update saving set balance=balance+"+tamt+" where pin="+tpin+";");
                                                        if (rs2==0) {
                                                            int rs3=st.executeUpdate("update current set balance=balance+"+tamt+" where pin="+tpin+";");   
                                                        }
                                                        account.get(tpin).trns.add("Amount "+tamt+" Recived from Account N/O:"+tpin);
                                                        int rs3=st.executeUpdate("insert transaction_details value("+tpin+",'Amount "+tamt+" Recived From Account N/O:"+pin+"');");
                                                        System.out.println("Amount Transfered Succesfully."); 
                                                    }
                                                    catch(Exception e){
                                                        e.printStackTrace();
                                                    }
                                                    break;
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
                                        if (account.get(cpin).ln.size()>0) {
                                                int sno=1;
                                                for (double i : account.get(cpin).ln) {
                                                    System.out.println("Loan No:"+sno+++" Loan Amount:"+i);
                                                }
                                                System.out.println("you have "+account.get(cpin).ln.size()+" loans");
                                                System.out.println("Enter the Loan No you want to pay");
                                                int lnpay=muf.nextInt();
                                                account.get(cpin).PayLoan(lnpay,cpin);break;
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
                            else if(choice2==6)break;
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
                        System.out.println("Enter your name.");
                        muf.nextLine();
                        String name=muf.nextLine();
                        if(account.get(pin).name.equals(name)){
                            try {
                                Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","1234");
                                Statement st=con.createStatement();
                                int r=st.executeUpdate("delete from saving where pin="+pin+";");
                                if (r==0) {
                                    int r1=st.executeUpdate("delete from current where pin="+pin+";");   
                                }
                                int r2=st.executeUpdate("delete from loan where pin="+pin+";");
                                int r3=st.executeUpdate("delete from transaction_details where pin="+pin+";");   
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            System.out.println("Your account removed "+account.get(pin).name);
                            account.remove(pin);break;
                        }
                        else{
                            System.out.println("Enter correct Name.");
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