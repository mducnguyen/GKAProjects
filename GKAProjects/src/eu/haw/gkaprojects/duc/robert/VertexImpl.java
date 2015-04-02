package eu.haw.gkaprojects.duc.robert;

public class VertexImpl implements Vertex{
	private String _label;
	private int _attribute;
	
	public VertexImpl(String label) {
		_label = label;
		_attribute = 0;
	}
	
	public VertexImpl(String label, int attribute){
		_label = label;
		_attribute = attribute;
	}
	
	@Override
	public String getLabel() {
		return _label;
	}
	
	@Override
	public int getAttribut() {
		return _attribute;
	}

	@Override
	public boolean equals(Object obj) {
		boolean result = false;
        if (obj instanceof Vertex)
        {
            VertexImpl vergleichsVertex = (VertexImpl) obj;
            result = _label.equals(vergleichsVertex._label); //&& _attribute == vergleichsVertex._attribute ;
        }
        return result;
	}
	
	@Override
	public int hashCode() {
		return _label.hashCode() ;
	}
}
