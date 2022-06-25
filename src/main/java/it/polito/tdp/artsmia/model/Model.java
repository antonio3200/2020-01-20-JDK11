package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao;
	private SimpleWeightedGraph<Artista,DefaultWeightedEdge> grafo;
	private List<Artista> vertici;
	private Map<Integer,Artista> idMap;
	private List<Arco> adiacenze;
	
	public Model() {
		this.dao= new ArtsmiaDAO();
		this.vertici= new ArrayList<>();
		this.idMap= new HashMap<>();
		this.adiacenze=new ArrayList<>();
	}
	
	public List<String> getRuoli(){
		return this.dao.getRuoli();
	}
	
	public void creaGrafo(String ruolo) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.vertici=this.dao.getArtisti(ruolo,this.idMap);
		//aggiungo vertici
		Graphs.addAllVertices(this.grafo, this.vertici);
		//aggiungo archi
		List<Arco> archi= this.dao.getArco(idMap,ruolo);
		this.adiacenze=archi;
		for(Arco a : archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getA1(), a.getA2(), a.getPeso());
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public List<Artista> getVertici(){
		return this.vertici;
	}
	
	public List<Arco> getArchi(){
		List<Arco> result= this.adiacenze;
		Collections.sort(result);
		return result;
	}
}
