package it.polito.tdp.artsmia.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	private List<ArtObject> artObjects;
	private Graph<ArtObject, DefaultWeightedEdge> graph;

	/**
	 * Popola la lista artObjects (leggendo dal DB) e crea il grafo
	 */
	public void creaGrafo() {
		// leggi lista degli oggetti dal DB
		ArtsmiaDAO DAO = new ArtsmiaDAO();
		this.artObjects = DAO.listObjects();
		// crea grafo
		this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class); // creato con 0 vertici e 0 archi

		// aggiungi vertici
		// for(ArtObject ao:this.artObjects)
		// this.graph.addVertex(ao); //ottengo tanti vertici ma per ora ho 0 archi
		Graphs.addAllVertices(this.graph, this.artObjects);
		// aggiungi archi(con il loro peso)
		for (ArtObject aop : this.artObjects) {
			for (ArtObject aoa : this.artObjects) {
				if (!aop.equals(aoa) && aop.getId() < aoa.getId()) {// escludo iloop
					int peso = exhibitionComuni(aop, aoa);
					// System.out.format("(%d, %d) peso %d\n", aop.getId(),aoa.getId(),peso);
					if (peso != 0) {
						 System.out.format("(%d, %d) peso %d\n", aop.getId(),aoa.getId(),peso);

						// DefaultWeightedEdge e=this.graph.addEdge(aop, aoa);
						// addEdge facciamo il doppio del lavoro perchè non è orientato
						// mi viene restituito null se è già stato inserito l'arco
						// serebbe un problema(nullpointexception el passaggio seguente
						// quindi aggiungo condizione nel'if
						// graph.setEdgeWeight(e, peso);

						Graphs.addEdge(this.graph, aop, aoa, peso);
					}
				}
			}
		}
	}

	private int exhibitionComuni(ArtObject aop, ArtObject aoa) {
		ArtsmiaDAO DAO = new ArtsmiaDAO();
		int comuni = DAO.contaExibitionComuni(aop, aoa);

		return comuni;
	}
}
