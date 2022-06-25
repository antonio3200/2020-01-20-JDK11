package it.polito.tdp.artsmia.model;

public class Arco implements Comparable<Arco> {

	Artista a1;
	Artista a2;
	Integer peso;
	public Arco(Artista a1, Artista a2, Integer peso) {
		super();
		this.a1 = a1;
		this.a2 = a2;
		this.peso = peso;
	}
	public Artista getA1() {
		return a1;
	}
	public void setA1(Artista a1) {
		this.a1 = a1;
	}
	public Artista getA2() {
		return a2;
	}
	public void setA2(Artista a2) {
		this.a2 = a2;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return this.getA1().toString()+" - "+this.getA2().toString()+" ("+this.getPeso()+")";
	}
	@Override
	public int compareTo(Arco o) {
		return -this.getPeso().compareTo(o.getPeso());
	}
	
	
	
}
