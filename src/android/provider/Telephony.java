package android.provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;
import android.text.util.Regex;

public class Telephony {
	public static final class Mms {
		public static final Pattern NAME_ADDR_EMAIL_PATTERN = Pattern.compile("\\s*(\"[^\"]*\"|[^<>\"]+)\\s*<([^<>]+)>\\s*");
		
		public static boolean isEmailAddress(String address) {
			if (TextUtils.isEmpty(address)) {
				return false;
			}
			
			String s = extractAddrSpec(address);
			Matcher match = Regex.EMAIL_ADDRESS_PATTERN.matcher(s);
			return match.matches();
		}
		
		public static String extractAddrSpec(String address) {
			Matcher match = NAME_ADDR_EMAIL_PATTERN.matcher(address);
			
			if (match.matches()) {
				return match.group(2);
			}
			return address;
		}
	}
}
