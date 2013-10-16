package info.thezero.eclipse.pdt.restapi.codeassist;

import java.util.Collection;

import info.thezero.eclipse.pdt.restapi.uri.UriMapCollection;
import info.thezero.eclipse.pdt.restapi.uri.UriSuggestion;

import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionStrategy;
import org.eclipse.php.core.codeassist.IElementFilter;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.strategies.AbstractCompletionStrategy;

@SuppressWarnings("restriction")
public class RestApiCompletionStrategy extends AbstractCompletionStrategy implements ICompletionStrategy {
	public RestApiCompletionStrategy(ICompletionContext context) {
		super(context);
	}

	public RestApiCompletionStrategy(ICompletionContext context,
			IElementFilter elementFilter) {
		super(context, elementFilter);
	}

	@Override
	public void apply(ICompletionReporter reporter) throws Exception {
		// TODO Auto-generated method stub
		RestApiCompletionContext context = (RestApiCompletionContext) getContext();

		Collection<UriSuggestion> suggestions = UriMapCollection.getDefault().suggest(context.getTrigger(), context.getUri());
		if (suggestions != null) {
			for (UriSuggestion suggestion : suggestions) {
				reporter.reportKeyword(suggestion.getSuggestionPart(), "", getReplacementRange(context));
			}
		}
	}

}
