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
		// String userName = "AWUser1";
		Hashtable env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL,
				"ldap://127.0.0.1:10389/dc=example,dc=com");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		// env.put(Context.SECURITY_PRINCIPAL, new String("agileworks" + "\\" +
		// userName));
		// env.put(Context.SECURITY_CREDENTIALS, "$password1");

		DirContext ctx = null;
		NamingEnumeration results = null;
		try {
			ctx = new InitialDirContext(env);
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			results = ctx.search("", "(objectclass=person)", controls);
			showResult(results);

			// TODO Add node
			Person person = new Person();
			person.setDescription("Added persone");
			try {
				ctx.bind("cn=person2", person);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			System.out.println("------======Node added======-------");
			results = ctx.search("", "(objectclass=person)", controls);

			results = ctx.search("", "(objectclass=person)", controls);
			showResult(results);

			// TODO delete node
			System.out.println("------======Node deleted======-------");
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
				NamingEnumeration<? extends Attribute> allAttributes = attributes
						.getAll();
				Attribute currentAttribute = null;
				while (allAttributes.hasMore()) {
					currentAttribute = allAttributes.next();
					System.out.println(currentAttribute.getID() + " : "
							+ currentAttribute.get());
				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static void addPerson() {

	}

}
/*
import java.util.Hashtable;
import java.util.Properties;
import java.util.jar.Attributes;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

    public class LdapProgram {  


            public static void main(String[] args) {  

                 Hashtable env = new Hashtable();
                 env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
                 env.put(Context.PROVIDER_URL, "ldap://localhost:10389");
                 env.put(Context.SECURITY_AUTHENTICATION, "simple");
                 env.put(Context.SECURITY_PRINCIPAL,"uid=admin,ou=system"); // specify the username
                 env.put(Context.SECURITY_CREDENTIALS,"secret");// specify the password
                // TODO code application logic here  

                          // entry's DN 
           String entryDN = "uid=user1,ou=system";  

            // entry's attributes  

            Attribute cn = new BasicAttribute("cn", "Test User2");  
            Attribute sn = new BasicAttribute("sn", "Test2");  
            Attribute mail = new BasicAttribute("mail", "newuser@foo.com");  
            Attribute phone = new BasicAttribute("telephoneNumber", "+1 222 3334444");   
                Attribute oc = new BasicAttribute("objectClass");  
            oc.add("top");  
            oc.add("person");  
            oc.add("organizationalPerson");  
            oc.add("inetOrgPerson");  
            DirContext ctx = null;  

            try {  
                // get a handle to an Initial DirContext  
                ctx = new InitialDirContext(env);  

                // build the entry  
                BasicAttributes entry = new BasicAttributes();  
                entry.put(cn);  
                entry.put(sn);  
                entry.put(mail);  
                entry.put(phone);  

                entry.put(oc);  

                // Add the entry  

                ctx.createSubcontext(entryDN, entry);  
      //          System.out.println( "AddUser: added entry " + entryDN + ".");  

            } catch (NamingException e) {  
                System.err.println("AddUser: error adding entry." + e);  
            }  
         }  
    }  
*/