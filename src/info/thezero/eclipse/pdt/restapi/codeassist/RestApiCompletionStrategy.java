package info.thezero.eclipse.pdt.restapi.codeassist;

import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionStrategy;
import org.eclipse.php.core.codeassist.IElementFilter;
import org.eclipse.php.internal.core.codeassist.ICompletionReporter;
import org.eclipse.php.internal.core.codeassist.strategies.AbstractCompletionStrategy;

@SuppressWarnings("restriction")
public class RestApiCompletionStrategy extends AbstractCompletionStrategy implements ICompletionStrategy {

	public RestApiCompletionStrategy(ICompletionContext context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public RestApiCompletionStrategy(ICompletionContext context,
			IElementFilter elementFilter) {
		super(context, elementFilter);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void apply(ICompletionReporter reporter) throws Exception {
		// TODO Auto-generated method stub
		RestApiCompletionContext context = (RestApiCompletionContext) getContext();

		for (String apiUri : getUris()) {
			if (apiUri.startsWith(context.getUri())) {
				reporter.reportKeyword(apiUri, "", getReplacementRange(context));
			}
		}
	}

	private String[] getUris() {
		return new String[] {
			"users/:username",
			"users/:username/profile",
			"users/:username/account",
			"login/sso"
		};
	}

}
