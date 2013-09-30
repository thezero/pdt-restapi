package info.thezero.eclipse.pdt.restapi.uri;

public class UriSuggestion {
	private String uri;
	private String suggestionPart;

	public UriSuggestion(String uri, String suggestionPart) {
		this.uri = uri;
		this.suggestionPart = suggestionPart;
	}

	public String getUri() {
		return uri;
	}

	public String getSuggestionPart() {
		return suggestionPart;
	}

}
