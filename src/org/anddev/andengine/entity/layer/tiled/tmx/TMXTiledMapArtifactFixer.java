package org.anddev.andengine.entity.layer.tiled.tmx;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author Nicolas Gramlich
 * @since 20:39:47 - 28.07.2010
 */
public class TMXTiledMapArtifactFixer {
	// ===========================================================
	// Constants
	// ===========================================================

	// ===========================================================
	// Fields
	// ===========================================================

	// ===========================================================
	// Constructors
	// ===========================================================

	public static void main(final String[] args) throws ParseException {
		final Options options = new Options();

		options.addOption("f", "file", true, "Filename of the tileset to fix.");
		options.addOption("o", "out", true, "Filename of the fixed tileset.");
		options.addOption("m", true, "Margin of the existing tileset.");
		options.addOption("s", true, "Spacing of the existing tileset.");
		options.addOption("w", true, "Width of a tile.");
		options.addOption("h", true, "Height of a tile.");
		options.addOption("u", "usage", false, "HELP !!!");

		final BasicParser parser = new BasicParser();
		final CommandLine cl = parser.parse(options, args);

		try {
			if(cl.hasOption('u')) {
				final HelpFormatter f = new HelpFormatter();
				f.printHelp("TMXTiledMapArtifactFixer-Help", options);
			} else {
				final String filename = cl.getOptionValue("f");
				final String outFilename = cl.getOptionValue("o");
				final int tileWidth = Integer.parseInt(cl.getOptionValue("w"));
				final int tileHeight = Integer.parseInt(cl.getOptionValue("h"));
				final int margin = Integer.parseInt(cl.getOptionValue("m", "0"));
				final int spacing = Integer.parseInt(cl.getOptionValue("s", "0"));

				fix(filename, outFilename, tileWidth, tileHeight, margin, spacing);
			}
		} catch (final Throwable t) {
			final HelpFormatter f = new HelpFormatter();
			f.printHelp("TMXTiledMapArtifactFixer", options);
		}
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	private static void fix(final String pFilename, final String pOutputFilename, final int pTileWidth, final int pTileHeight, final int pMargin, final int pSpacing) throws IOException {
		final File sourceFile = new File(pFilename);
		if(!sourceFile.isFile()) {
			throw new IllegalArgumentException("Not a file: " + pFilename);
		}
		System.out.println("Fixing:");

		final BufferedImage img = ImageIO.read(sourceFile);

		final int imageWidth = img.getWidth();
		final int imageHeight = img.getHeight();

		final int columnCount = determineCount(imageWidth, pTileWidth, pMargin, pSpacing);
		final int rowCount = determineCount(imageHeight, pTileHeight, pMargin, pSpacing);

		final BufferedImage out = new BufferedImage(imageWidth + columnCount * 2, imageHeight + rowCount * 2, BufferedImage.TYPE_INT_ARGB);
		final Graphics g = out.getGraphics();

		for(int row = 0; row < rowCount; row++) {
			for(int column = 0; column < columnCount; column++) {
				/* Upper-Left coordinates in the source image. */
				final int sx = pMargin + column * pTileWidth + column * pSpacing;
				final int sy = pMargin + row * pTileHeight + row * pSpacing;
				
				final int sx2 = sx + pTileWidth;
				final int sy2 = sy + pTileHeight;

				/* Upper-Left coordinates in the destination image. */
				final int dx = sx + 2 * column + 1;
				final int dy = sy + 2 * row + 1;
				
				final int dx2 = dx + pTileWidth;
				final int dy2 = dy + pTileHeight;
				
				/* Draw the spacing on each side if it exists. */
				if(pSpacing > 0) {
					g.drawImage(img, dx - (pSpacing + 1), dy, dx - pSpacing, dy2, sx - pSpacing, sy, sx, sy2, null); /* Left */
					g.drawImage(img, dx2 + pSpacing, dy, dx2 + (pSpacing + 1), dy2, sx2, sy, sx2 + pSpacing, sy2, null); /* Right */
					g.drawImage(img, dx, dy - (pSpacing + 1), dx2, dy - pSpacing, sx, sy - pSpacing, sx2, sy, null); /* Top */
					g.drawImage(img, dx, dy2 + pSpacing, dx2, dy2 + (pSpacing + 1), sx, sy2, sx2, sy2 + pSpacing, null); /* Bottom */
				}
				
				/* Draw the tile with 1 px offset to each side. */
				g.drawImage(img, dx - 1, dy, dx2 - 1, dy2, sx, sy, sx2, sy2, null); /* Left */
				g.drawImage(img, dx + 1, dy, dx2 + 1, dy2, sx, sy, sx2, sy2, null); /* Right */
				g.drawImage(img, dx, dy - 1, dx2, dy2 - 1, sx, sy, sx2, sy2, null); /* Top */
				g.drawImage(img, dx, dy + 1, dx2, dy2 + 1, sx, sy, sx2, sy2, null); /* Bottom */ 
				
				/* Draw the tile where it actually belongs. */
				g.drawImage(img, dx, dy, dx2, dy2, sx, sy, sx2, sy2, null);
				
				System.out.print("x");
			}
			System.out.print("\n");
		}
		System.out.print("Saving... ");
		ImageIO.write(out, "png", pOutputFilename != null ? new File(pOutputFilename) : generateOutputFile(sourceFile));
		System.out.print("done.");
	}

	static File generateOutputFile(final File pSourceFile) {
		return new File(pSourceFile.getParentFile() + File.separator + "fixed_" + pSourceFile.getName());
	}

	static int determineCount(final int pTotalExtent, final int pTileExtent, final int pMargin, final int pSpacing) {
		int count = 0;
		int remainingExtent = pTotalExtent;

		remainingExtent -= pMargin * 2;

		while(remainingExtent > 0) {
			remainingExtent -= pTileExtent;
			remainingExtent -= pSpacing;
			count++;
		}

		return count;
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
