package eu.haw.gkaprojects.duc.robert;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.jgraph.JGraph;
import org.jgrapht.Graph;
import org.jgrapht.WeightedGraph;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedPseudograph;
import org.jgrapht.graph.DirectedWeightedPseudograph;
import org.jgrapht.graph.Pseudograph;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.graph.WeightedPseudograph;

public class Filereader {

	public static final int DIRECTED = 1;
	public static final int ATTRIBUTED = 3;
	public static final int WEIGHTED = 5;
	public static final boolean attributed = true;

	enum GraphType {
		DIRECTED_WEIGHTED, DIRECTED_UNWEIGHTED, UNDIRECTED_WEIGHTED, UNDIRECTED_UNWEIGHTED
	}

	private Graph<Vertex, DefaultEdge> _graph;

	/**
	 * 
	 * @param Path
	 *            Path to file
	 */
	public Filereader(String Path) {
		_graph = readgraph(Path);
	}

	private Graph<Vertex, DefaultEdge> readgraph(String path) {
		String strData = "";
		int graphType = 0;
		// Read Text File
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			// read header
			sb.append(line);
			graphType = chooseGraphType(sb.toString());

			// read graph
			sb = new StringBuilder();
			line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
				// sb.append(line);
			}
			strData = sb.toString();

		} catch (FileNotFoundException e) {
			System.err.println("ERROR: Cannot find text data!!!" + path);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		String[] edgesArray = strData.split(System.lineSeparator());
		return createGraph(edgesArray, graphType);
	}

	private Graph<Vertex, DefaultEdge> createGraph(String[] edgesArray,
			int graphType) {
		//Trim edges
		List<String> tempList = new ArrayList<String>();
		for (String edge : edgesArray) {
			if(!edge.equals("")){
				tempList.add(edge);
			}
		}
		String[] tempStrings = new String[tempList.size()];
		tempStrings= tempList.toArray(tempStrings);
		edgesArray = tempStrings;
		
		Graph<Vertex, DefaultEdge> graph = null;

		switch (graphType) {
		case 0:
			graph = createSimpleGraph(edgesArray);
			break;
		case DIRECTED:
			graph = createSimpleDrirectedGraph(edgesArray);
			break;
		case ATTRIBUTED:
			graph = createSimpleGraphWithAttribute(edgesArray);
			break;
		case DIRECTED + ATTRIBUTED:
			graph = createSimpleDirectedGraphAttribute(edgesArray);
			break;
		case WEIGHTED:
			graph = createSimpleWeightedGraph(edgesArray);
			break;
		case DIRECTED + WEIGHTED:
			graph = createSimpleDirectedWeightedGraph(edgesArray);
			break;
		case ATTRIBUTED + WEIGHTED:
			graph = createSimpleWeightedGraphWithAttribute(edgesArray);
			;
			break;
		case DIRECTED + ATTRIBUTED + WEIGHTED:
			graph = createSimpleDirectedWeightedGraphWithAttribute(edgesArray);
			break;
		default:
			System.out.println("Cannot find type of Graph!");
		}
		System.out.println("createGraph done: ");
		return graph;
	}

	private Graph<Vertex, DefaultEdge> createSimpleDirectedWeightedGraphWithAttribute(
			String[] edgesArray) {
		Graph<Vertex, DefaultEdge> graph = new DirectedWeightedPseudograph<>(
				DefaultWeightedEdge.class);

		for (String StringEdge : edgesArray) {
			String trimedEdge = StringEdge.replaceAll("::", ",");
			trimedEdge = trimedEdge.replaceAll(" ", "");
			String[] verticesAndWeight = trimedEdge.split(",");
			String[] vertex1 = verticesAndWeight[0].split(":");
			String[] vertex2 = verticesAndWeight[1].split(":");
			
			Vertex v1 = new VertexImpl(vertex1[0],Integer.valueOf(vertex1[1]));
			Vertex v2 = new VertexImpl(vertex2[0],Integer.valueOf(vertex2[1]));
			graph.addVertex(v1);
			graph.addVertex(v2);
			DefaultWeightedEdge egde = (DefaultWeightedEdge) graph.addEdge(v1,v2);
			((AbstractBaseGraph<Vertex, DefaultEdge>) graph).setEdgeWeight(egde, Double.valueOf(verticesAndWeight[2]));
		}
		
		System.out.println(edgesArray.length);
		return graph;
	}

	private Graph<Vertex, DefaultEdge> createSimpleGraphWithAttribute(
			String[] edgesArray) {
		Graph<Vertex, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);

		for (String edge : edgesArray) {
			String trimedEdge = edge.replaceAll(" ", "");
			String[] verticesAndWeight = trimedEdge.split(",");
			String[] vertex1 = verticesAndWeight[0].split(":");
			String[] vertex2 = verticesAndWeight[1].split(":");
			
			Vertex v1 = new VertexImpl(vertex1[0],Integer.valueOf(vertex1[1]));
			Vertex v2 = new VertexImpl(vertex2[0],Integer.valueOf(vertex2[1]));
			graph.addVertex(v2);
			graph.addEdge(v1, v2);
		}
		
		System.out.println(edgesArray.length);
		return graph;
	}

	private Graph<Vertex, DefaultEdge> createSimpleDirectedGraphAttribute(
			String[] edgesArray) {

		Graph<Vertex, DefaultEdge> graph = new DirectedPseudograph<>(
				DefaultEdge.class);

		for (String edge : edgesArray) {
			String trimedEdge = edge.replaceAll(" ", "");
			String[] verticesAndWeight = trimedEdge.split(",");
			String[] vertex1 = verticesAndWeight[0].split(":");
			String[] vertex2 = verticesAndWeight[1].split(":");
			
			Vertex v1 = new VertexImpl(vertex1[0],Integer.valueOf(vertex1[1]));
			Vertex v2 = new VertexImpl(vertex2[0],Integer.valueOf(vertex2[1]));
			graph.addVertex(v1);
			graph.addVertex(v2);
			graph.addEdge(v1, v2);
		}

		System.out.println(edgesArray.length);
		return graph;
	}

	private Graph<Vertex, DefaultEdge> createSimpleWeightedGraphWithAttribute(
			String[] edgesArray) {
		Graph<Vertex, DefaultEdge> graph = new WeightedPseudograph<>(
				DefaultWeightedEdge.class);

		for (String StringEdge : edgesArray) {
			String trimedEdge = StringEdge.replaceAll("::", ",");
			trimedEdge = trimedEdge.replaceAll(" ", "");
			String[] verticesAndWeight = trimedEdge.split(",");
			String[] vertex1 = verticesAndWeight[0].split(":");
			String[] vertex2 = verticesAndWeight[1].split(":");
			
			Vertex v1 = new VertexImpl(vertex1[0],Integer.valueOf(vertex1[1]));
			Vertex v2 = new VertexImpl(vertex2[0],Integer.valueOf(vertex2[1]));
			graph.addVertex(v1);
			graph.addVertex(v2);
			DefaultWeightedEdge egde = (DefaultWeightedEdge) graph.addEdge(v1,v2);
			((AbstractBaseGraph<Vertex, DefaultEdge>) graph).setEdgeWeight(egde, Double.valueOf(verticesAndWeight[2]));
		}

		System.out.println(edgesArray.length);
		return graph;
	}

	private Graph<Vertex, DefaultEdge> createSimpleDirectedWeightedGraph(
			String[] edgesArray) {
		Graph<Vertex, DefaultEdge> graph = new DirectedWeightedPseudograph<>(
				DefaultWeightedEdge.class);

		for (String StringEdge : edgesArray) {
			String trimedEdge = StringEdge.replaceAll("::", ",");
			trimedEdge = trimedEdge.replaceAll(" ", "");
			String[] verticesAndWeight = trimedEdge.split(",");
		
			Vertex v1 = new VertexImpl(verticesAndWeight[0]);
			Vertex v2 = new VertexImpl(verticesAndWeight[1]);
			graph.addVertex(v1);
			graph.addVertex(v2);
			DefaultWeightedEdge egde = (DefaultWeightedEdge) graph.addEdge(v1,v2);
			((AbstractBaseGraph<Vertex, DefaultEdge>) graph).setEdgeWeight(egde, Double.valueOf(verticesAndWeight[2]));
		}

		System.out.println(edgesArray.length);
		return graph;
	}

	private Graph<Vertex, DefaultEdge> createSimpleWeightedGraph(
			String[] edgesArray) {
		Graph<Vertex, DefaultEdge> graph = new WeightedPseudograph<>(
				DefaultWeightedEdge.class);

		for (String StringEdge : edgesArray) {
			String trimedEdge = StringEdge.replaceAll("::", ",");
			trimedEdge = trimedEdge.replaceAll(" ", "");
			String[] verticesAndWeight = trimedEdge.split(",");
			
			Vertex v1 = new VertexImpl(verticesAndWeight[0]);
			Vertex v2 = new VertexImpl(verticesAndWeight[1]);
			graph.addVertex(v1);
			graph.addVertex(v2);
			DefaultWeightedEdge egde = (DefaultWeightedEdge) graph.addEdge(v1,v2);
			((AbstractBaseGraph<Vertex, DefaultEdge>) graph).setEdgeWeight(egde, Double.valueOf(verticesAndWeight[2]));
		}

		System.out.println(edgesArray.length);
		return graph;
	}

	private Graph<Vertex, DefaultEdge> createSimpleDrirectedGraph(
			String[] edgesArray) {

		Graph<Vertex, DefaultEdge> graph = new DirectedPseudograph<>(
				DefaultEdge.class);
		System.out.println(edgesArray.length);
		for (String edge : edgesArray) {
			String[] verticesOfEdge = edge.split(",");
			System.out.println(verticesOfEdge.length);
			if(verticesOfEdge.length < 2){
				Vertex v = new VertexImpl(verticesOfEdge[0].replaceAll(" ", ""));
				graph.addVertex(v);
				continue;
			}
			Vertex v1 = new VertexImpl(verticesOfEdge[0].replaceAll(" ", ""));
			Vertex v2 = new VertexImpl(verticesOfEdge[1].replaceAll(" ", ""));
			graph.addVertex(v1);
			graph.addVertex(v2);
			graph.addEdge(v1, v2);
		}

		
		return graph;
	}

	private Graph<Vertex, DefaultEdge> createSimpleGraph(String[] edgesArray) {
		Graph<Vertex, DefaultEdge> graph = new Pseudograph<>(DefaultEdge.class);

		for (String edge : edgesArray) {
			String[] verticesOfEdge = edge.split(",");
			if(verticesOfEdge.length < 2){
				Vertex v = new VertexImpl(verticesOfEdge[0].replaceAll(" ", ""));
				graph.addVertex(v);
				continue;
			}
			Vertex v1 = new VertexImpl(verticesOfEdge[0].replaceAll(" ", ""));
			Vertex v2 = new VertexImpl(verticesOfEdge[1].replaceAll(" ", ""));
			
			graph.addVertex(v1);
			graph.addVertex(v2);
			graph.addEdge(v1, v2);
		}
		System.out.println(edgesArray.length);
		return graph;
	}

//	private Set<Vertex> makeSetAttributedVertices(
//			Set<String> setOfStringVertices) {
//		Set<Vertex> setOfVerticesAttributed = new HashSet<Vertex>();
//
//		// create vertices with attribute
//		for (String vertexString : setOfStringVertices) {
//			String[] vertexWithAttribute = vertexString.split(":");
//			setOfVerticesAttributed.add(new VertexImpl(vertexWithAttribute[0],
//					Integer.valueOf(vertexWithAttribute[1])));
//		}
//
//		return setOfVerticesAttributed;
//	}
//
//	private Set<Vertex> makeSetOfNonattributedVetices(
//			Set<String> setOfStringVertices) {
//
//		Set<Vertex> setOfvertexNonattributed = new HashSet<Vertex>();
//
//		// create vertices without attribute
//		for (String vertex : setOfStringVertices) {
//			setOfvertexNonattributed.add(new VertexImpl(vertex));
//		}
//
//		return setOfvertexNonattributed;
//	}

	private int chooseGraphType(String header) {
		header = header.trim();
		String[] headerArr = header.split("#");

		int graphType = 0;
		for (String string : headerArr) {
			if (string.equals("directed")) {
				graphType += DIRECTED;
			} else if (string.equals("attributed")) {
				graphType += ATTRIBUTED;
			} else if (string.equals("weighted")) {
				graphType += WEIGHTED;
			}
		}

		return graphType;
	}
	
	public Graph<Vertex, DefaultEdge> getGraph() {
		return _graph;
	}
}
