package it.polito.tdp.crimes.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.crimes.db.EventsDao;

public class Model {
	EventsDao dao;
	
	
	//percorso che tocchi il maggior numero di vertici, uso size della lista e non pesp
	
	private List<String> best;
	Graph<String, DefaultWeightedEdge> grafo;
	
	public Model () {
		dao= new EventsDao();
	}
	
	
	public void creaGrafo(String categoria, int mese) {
		grafo= new SimpleWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, dao.getVertici(categoria, mese));
		for (Adiacenza a: dao.getArchi(categoria, mese)) {
			Graphs.addEdgeWithVertices(this.grafo, a.getV1(),a.getV2(), a.getPeso());
		}
		
		//TODO riempire la tendina degli archi
		System.out.println("Grafo creato!");
		System.out.println("# VERTICI: "+this.grafo.vertexSet().size());
		System.out.println("# ARCHI: "+this.grafo.edgeSet().size());
		
	}


	public List<String> aggiungiCategoria() {
		List<String> categorie= dao.getCategorie();
			return categorie;
	}
	
	public List<Adiacenza> getArchiMaggioriPesoMedio() {
		double pesoTot=0.0;
		for (DefaultWeightedEdge e: this.grafo.edgeSet()) {
			pesoTot+=this.grafo.getEdgeWeight(e);
			
		}
		double avg= pesoTot/this.grafo.edgeSet().size();
		List<Adiacenza> result= new ArrayList<>();
		for (DefaultWeightedEdge e: this.grafo.edgeSet()) {
			if (this.grafo.getEdgeWeight(e)>avg)
				result.add(new Adiacenza(this.grafo.getEdgeSource(e), this.grafo.getEdgeTarget(e), (int) this.grafo.getEdgeWeight(e)));
			
		}
		return result;
	}
	
	
	public List<String> calcolaPercorso (String sorgente, String destinazione) {
		best=new LinkedList<>();
		List<String> parziale= new LinkedList<>();
		parziale.add(sorgente);
		cerca(parziale, destinazione);
		return best;
		
		
	}


	private void cerca(List<String> parziale, String destinazione) {
		//condizione di terminazione
		//mi fermo quando arrivo a destinazione
		if (parziale.get(parziale.size()-1).equals(destinazione)) {
			//è la soluzione migliore? Vedo la size, se quella di parziale è maggiore di 
			// size di best allorad devo sovrascriverla
			if (parziale.size()>best.size()) {
				best= new LinkedList<> (parziale);
			}
			return; 
		}
		//scorro i vicini dell'ultimo inserito e provo le varie strade
		for (String v: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if (!parziale.contains(v)) { //per evitare cicli
			parziale.add(v);
			this.cerca(parziale, destinazione);
			parziale.remove(parziale.size()-1);
			}
		}
	}


	public int nVertici() {
		return this.grafo.vertexSet().size();
		
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	
	}
	
	
	public List<Adiacenza> getArchi(String categoria, int mese) {
		
		List<Adiacenza> result= dao.getArchi(categoria, mese);
		return result;
	}
	
	
}
