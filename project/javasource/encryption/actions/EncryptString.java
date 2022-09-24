// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package encryption.actions;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import com.mendix.systemwideinterfaces.MendixRuntimeException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;

public class EncryptString extends CustomJavaAction<java.lang.String>
{
	private java.lang.String value;
	private java.lang.String key;
	private java.lang.String prefix;

	public EncryptString(IContext context, java.lang.String value, java.lang.String key, java.lang.String prefix)
	{
		super(context);
		this.value = value;
		this.key = key;
		this.prefix = prefix;
	}

	@java.lang.Override
	public java.lang.String executeAction() throws Exception
	{
		// BEGIN USER CODE
		if (this.value == null) 
			return null;
		if (this.prefix == null || this.prefix.isEmpty())
			throw new MendixRuntimeException("Prefix should not be empty");
		if(isLegacyAlgorithm(this.prefix))
			throw new MendixRuntimeException(String.format(
					"The used prefix is no longer supported for encryption. Please use '%s'.", NEW_PREFIX));
		if (!hasValidPrefix(this.prefix))
			throw new MendixRuntimeException(String.format("Invalid prefix used. Please use '%s'.", NEW_PREFIX));
		if (this.key == null || this.key.isEmpty())
			throw new MendixRuntimeException("Key should not be empty");
		if (this.key.length() != 32)
			throw new MendixRuntimeException("Key length should be 32");
		Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
		SecretKeySpec k = new SecretKeySpec(this.key.getBytes(), "AES");
		c.init(Cipher.ENCRYPT_MODE, k);

		byte[] encryptedData = c.doFinal(this.value.getBytes());
		byte[] iv = c.getIV();

		StringBuilder sb = new StringBuilder(this.prefix);
		sb.append(new String(Base64.getEncoder().encode(iv)));
		sb.append(';');
		sb.append(new String(Base64.getEncoder().encode(encryptedData)));
		return sb.toString();
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "EncryptString";
	}

	// BEGIN EXTRA CODE
	private static final String NEW_PREFIX = "{AES3}";
	private static final String LEGACY_PREFIX_REGEX = "^\\{AES2?}$";

	private boolean hasValidPrefix(String text) { return text.equals(NEW_PREFIX); }
	private boolean isLegacyAlgorithm(String text) { return text.matches(LEGACY_PREFIX_REGEX); }
	// END EXTRA CODE
}
