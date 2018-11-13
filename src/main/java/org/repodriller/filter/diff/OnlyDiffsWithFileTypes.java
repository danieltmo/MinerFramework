package org.repodriller.filter.diff;

import java.util.List;

import util.RDFileUtils;

/**
 * Only process diffs on files with certain file extensions.
 * 
 * @author Ayaan Kazerouni
 */
public class OnlyDiffsWithFileTypes implements DiffFilter {

	private List<String> fileExtensions;
	
	public OnlyDiffsWithFileTypes(List<String> fileExtensions) {
		this.fileExtensions = fileExtensions;
	}
	
	@Override
	public boolean accept(String diffEntryPath) {
		return RDFileUtils.fileNameHasIsOfType(diffEntryPath, this.fileExtensions);
	}

}
