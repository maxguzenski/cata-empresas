package com.eicoit.cataempresas.common;

public class Empresa {
	private String nome;
	private String site;
	private String email;
	private String contato;
	private String local;
	private String link;
	
	public Empresa() {
	}	
		
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}

	public String getContato() {
		return contato;
	}

	public void setContato(String contato) {
		this.contato = contato;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Empresa) {
			if (this.getNome() != null && ((Empresa)obj).getNome() != null) {
				return this.getNome().trim().equals( ((Empresa)obj).getNome().trim() );
			}
		}
		return false;
	}

	public int hashCode() {
		if (this.getNome() != null) return this.getNome().hashCode();
		return 0;
	}	
}
