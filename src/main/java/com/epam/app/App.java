package com.epam.app;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

public class App {

	public static void main(String[] args) {
		Hashtable<String, String> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY,
				"com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL,
				"ldap://127.0.0.1:10389/dc=example,dc=com");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");

		DirContext ctx = null;
		NamingEnumeration<?> results = null;
		try {
			ctx = new InitialDirContext(env);
			SearchControls controls = new SearchControls();
			controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			results = ctx.search("", "(objectclass=person)", controls);
			showResult(results);
			// Create 10 persones
			for (int i = 0; i < 10; i++) {
				addPerson(ctx, "persone_" + Math.random());
			}
			System.out.println("------======Nodes added======-------");
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
				System.out.println("===================================");
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static void addPerson(DirContext context, String personeName) {
		Attribute cn = new BasicAttribute("cn", personeName);
		Attribute sn = new BasicAttribute("sn", "example");
		Attribute description = new BasicAttribute("description",
				"Some description");
		Attribute oc = new BasicAttribute("objectClass");
		oc.add("top");
		oc.add("person");
		BasicAttributes entry = new BasicAttributes();
		entry.put(cn);
		entry.put(sn);
		entry.put(description);
		entry.put(oc);
		try {
			context.createSubcontext("cn=" + personeName, entry);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
