package info.thezero.eclipse.pdt.restapi.codeassist;

import info.thezero.eclipse.pdt.restapi.uri.Map;

import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionStrategy;
import org.eclipse.php.core.codeassist.IElementFilter;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.strategies.AbstractCompletionStrategy;

@SuppressWarnings("restriction")
public class RestApiCompletionStrategy extends AbstractCompletionStrategy implements ICompletionStrategy {
	private Map uriMap;

	public RestApiCompletionStrategy(ICompletionContext context) {
		super(context);
		this.uriMap = new Map();
	}

	public RestApiCompletionStrategy(ICompletionContext context,
			IElementFilter elementFilter) {
		super(context, elementFilter);
		this.uriMap = new Map();
	}

	@Override
	public void apply(ICompletionReporter reporter) throws Exception {
		// TODO Auto-generated method stub
		RestApiCompletionContext context = (RestApiCompletionContext) getContext();

		for (String apiUri : uriMap.suggest(context.getUri())) {
			reporter.reportKeyword(apiUri, "", getReplacementRange(context));
		}
	}

}
