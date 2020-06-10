/**
 * @Project MoocSearch
 *
 * Copyright 2019 SakuraKooi. All right reserved.
 *
 * This is a private project. Distribution is not allowed.
 * You needs ask SakuraKooi for the permission to using it.
 *
 * @Author SakuraKooi (sakurakoi993519867@gmail.com)
 */
package sakura.kooi.MoocSearch.utils;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 ** @version $Id: MultiLineLabel.java 6164 2009-02-19 18:11:32Z polle $
 ** @author Justin Tan A multi-line Label-like AWT component.
 **/
public class MultiLineLabel extends JPanel {
	private static final long serialVersionUID = -5598742426220399277L;
	protected int fontAttributes = Font.PLAIN;
	protected float alignment;
	protected Color col = null;
	protected int spacing = 0;

	/**
	 ** Constructor - make a multiline label
	 **/
	public MultiLineLabel(final String text, final float alignment) {
		this.alignment = alignment;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		if (text != null) {
			setText(text);
		}
	}

	/**
	 ** Constructor, defaults to centered text
	 **/
	public MultiLineLabel(final String text) {
		this(text, LEFT_ALIGNMENT);
	}

	/**
	 * Constructor, empty with the given alignment
	 */
	public MultiLineLabel(final float alignment) {
		this(null, alignment);
	}

	/**
	 * Constructor, empty with the given alignment and line spacing
	 */
	public MultiLineLabel(final float alignment, final int spacing) {
		this(null, alignment);
		this.spacing = spacing;
	}

	/**
	 ** Constructor - make an empty multiline label
	 **/
	public MultiLineLabel() {
		this(null, LEFT_ALIGNMENT);
	}

	private String text = "";

	public void setText(final String text) {
		// clear the existing lines from the panel
		this.text = text;
		removeAll();
		addText(text);
	}

	public String getText() {
		return text;
	}

	private void addText(final String text) {
		addText(text, 12);
	}

	private void addText(final String text, final int size) {
		if (spacing > 0) {
			add(Box.createVerticalStrut(spacing));
		}

        final String[] strs = splitLines(text);
		JLabel l;
		final Font font = new Font("SansSerif", fontAttributes, size);

		for (int i = 0; strs != null && i < strs.length; i++) {
			l = new JLabel(strs[i]);
			l.setFont(font);
			l.setAlignmentX(alignment);

			if (col != null) {
				l.setForeground(col);
			}

			add(l);
		}
	}

	/**
	 * Splits "string" by "Delimiter"
	 * 
	 * @param str
	 *            - the string to be split
	 * @param delimiter
	 *            - the field delimiter within str
	 * @returns an array of Strings
	 */
	public static String[] split(String str, final String delimiter) {
		final List<String> strings = new ArrayList<>();
		int start = 0;
		int len = str.length();
		final int dlen = delimiter.length();
		int offset = str.lastIndexOf(delimiter); // First of all, find the
		// Last occurance of the Delimiter
		// Stop empty delimiters
		if (dlen < 1)
			return null;
		else if (offset < 0) // one element
		{
			return new String[]{ str };
		}

		//
		// Append the delimiter onto the end if it doesn't already exit
		//
		if (len > offset + dlen) {
			str += delimiter;
			len += dlen;
		}

		do {
			// Get the new Offset
			offset = str.indexOf(delimiter, start);
			strings.add(str.substring(start, offset));

			// Get the new Start position
			start = offset + dlen;
		} while (start < len && offset != -1);

		// Convert the list into an Array of Strings
		final String[] result = new String[strings.size()];
		strings.toArray(result);
		return result;
	}

	/**
	 * Splits "string" into lines (stripping end-of-line characters)
	 * 
	 * @param str
	 *            - the string to be split
	 * @returns an array of Strings
	 */
	public static String[] splitLines(final String str) {
		return str == null ? null : split(str, "\n");
	}

	@Override
	public void setForeground(final Color col) {
		this.col = col;
		final Component[] components = getComponents();
		for (int i = 0; i < components.length; i++) {
			final Component component = components[i];
			component.setForeground(col);
		}
	}

	public void setItalic(final boolean italic) {
		if (italic) {
			fontAttributes |= Font.ITALIC;
		} else {
			fontAttributes &= ~Font.ITALIC;
		}
	}

	public void setBold(final boolean bold) {
		if (bold) {
			fontAttributes |= Font.BOLD;
		} else {
			fontAttributes &= ~Font.BOLD;
		}
	}
}
