package net.main;

import java.sql.*;

public class Customer {
    private int tid;
    private String username;
    private String password;
    private String cname;
    private String state;
    private String phone;
    private String email;
    private int ssn;

    public void Account(int taxId, String cUsername, String cPassword, String cName, String stateCode, String phoneNum, String cEmail, int SSN)
	{
        tid = taxId;
		username = cUsername;
		password = cPassword;
		cname = cName;
		state = stateCode;
		phone = phoneNum;
		email = cEmail;
        ssn = SSN;
	}

    public int getTid() {
        return tid;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return cname;
    }

    public String getState() {
        return state;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public int getSSN() {
        return ssn;
    }
}