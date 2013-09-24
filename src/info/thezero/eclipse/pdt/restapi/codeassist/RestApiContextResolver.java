package info.thezero.eclipse.pdt.restapi.codeassist;

import org.eclipse.php.core.codeassist.ICompletionContext;
import org.eclipse.php.core.codeassist.ICompletionContextResolver;
import org.eclipse.php.internal.core.codeassist.contexts.CompletionContextResolver;

@SuppressWarnings("restriction")
public class RestApiContextResolver extends CompletionContextResolver implements ICompletionContextResolver {
	public ICompletionContext[] createContexts() {
		return new ICompletionContext[] { new RestApiCompletionContext() };
	}

}
