package info.thezero.eclipse.pdt.restapi.codeassist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.php.internal.core.codeassist.contexts.QuotesContext;
import org.eclipse.php.internal.core.util.text.TextSequence;
import org.eclipse.dltk.core.CompletionRequestor;
import org.eclipse.dltk.core.ISourceModule;

@SuppressWarnings("restriction")
public class RestApiCompletionContext extends QuotesContext {
	private String uri;
	private String trigger;

	public boolean isValid(ISourceModule sourceModule, int offset, CompletionRequestor requestor) {

		if (super.isValid(sourceModule, offset, requestor)) {

			// take statement minus opening quotes
			TextSequence statementText = getStatementText();
			int quotesStart = readQuotesStartPosition(statementText, statementText.length());

			if (quotesStart == 0) {
				return false;
			}
			TextSequence contents = statementText.subTextSequence(quotesStart, statementText.length());

			if (contents.length() > 0 && contents.charAt(0) == '/') {
				this.uri = contents.toString().substring(1);
				this.trigger = getTriggerFromSequence(statementText, quotesStart);
				return true;
			}
		}

		return false;
	}

	public static int readQuotesStartPosition(CharSequence textSequence, int startPosition) {
		while (startPosition > 0) {
			char ch = textSequence.charAt(startPosition - 1);
			if (ch == '\'' || ch == '"') {
				if (startPosition > 1) {
					char prev = textSequence.charAt(startPosition - 2);
					if (prev != '\\') {
						// unescaped open quote
						break;
					}
				} else {
					// open quote as first char
					break;
				}
			}
			startPosition--;
		}
		return startPosition;
	}

	public static String getTriggerFromSequence(TextSequence textSequence, int quoteStart) {
		TextSequence opening = textSequence.subTextSequence(0, quoteStart);

		Pattern p = Pattern.compile("[\\s\\S]*\\b(\\w+)\\s*\\(\\s*.*");
		Matcher m = p.matcher(opening);

		if (!m.matches()) {
			return "";
		}

		return m.group(1);
	}


	public String getUri() {
		return this.uri;
	}

	public String getTrigger() {
		return trigger;
	}

}
