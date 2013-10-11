package com.epam.app;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class App {

	public static void main(String[] args) {
		//String userName = "AWUser1";
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, "ldap://127.0.0.1:10389/dc=example,dc=com");
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        //env.put(Context.SECURITY_PRINCIPAL, new String("agileworks" + "\\" + userName));
        //env.put(Context.SECURITY_CREDENTIALS, "$password1");


        DirContext ctx = null;
        NamingEnumeration results = null;
        try {
            ctx = new InitialDirContext(env);
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            results = ctx.search("", "(objectclass=person)", controls);
            showResult(results);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            if (results != null) {
                try {
                    results.close();
                } catch (Exception e) {
                }
            }
            if (ctx != null) {
                try {
                    ctx.close();
                } catch (Exception e) {
                }
            }
        }
	}
	
	public static void showResult(NamingEnumeration<?> result) {
        try {
			while (result.hasMore()) {
			    SearchResult searchResult = (SearchResult) result.next();
			    
			    System.out.println(searchResult.getName());
			    System.out.println(searchResult.getNameInNamespace());
			    
			    Attributes attributes = searchResult.getAttributes();
			    NamingEnumeration<? extends Attribute> allAttributes = attributes.getAll();
			    Attribute currentAttribute = null; 
			    while(allAttributes.hasMore()) {
			    	currentAttribute = allAttributes.next();
			    	System.out.println(currentAttribute.getID() + " : " + currentAttribute.get());
			    }
			}
		} catch (NamingException e) {			
			e.printStackTrace();
		}
	}
	
	public static void addPerson() {
		
	}
	
}
