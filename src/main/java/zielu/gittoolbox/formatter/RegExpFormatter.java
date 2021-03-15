package zielu.gittoolbox.formatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.Icon;
import jodd.util.StringBand;
import org.apache.commons.lang.StringUtils;
import zielu.gittoolbox.completion.FormatterIcons;

public class RegExpFormatter implements Formatter {
  private final Pattern pattern;

  private RegExpFormatter(Pattern pattern) {
    this.pattern = pattern;
  }

  public static RegExpFormatter create(String pattern) {
    if (StringUtils.isNotBlank(pattern)) {
      try {
        return new RegExpFormatter(Pattern.compile(pattern));
      } catch (PatternSyntaxException e) {
        return new RegExpFormatter(null);
      }
    } else {
      return new RegExpFormatter(null);
    }
  }

  @Override
  public Formatted format(String input) {
    if (pattern == null) {
      return new Formatted(input, false, "no pattern");
    } else {
      Matcher matcher = pattern.matcher(input);
      if (matcher.matches()) {
        return new Formatted(format(matcher), true, null);
      } else {
        return new Formatted(input, false, "pattern: " + pattern.pattern());
      }
    }
  }

  private String format(Matcher matcher) {
    int count = matcher.groupCount();
    StringBand result = new StringBand(count);
    for (int i = 1; i <= count; i++) {
      result.append(matcher.group(i));
    }
    return result.toString();
  }

  @Override
  public Icon getIcon() {
    return FormatterIcons.getRegExp();
  }
}
