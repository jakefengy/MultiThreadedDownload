
package com.team.xg.exception;

public class FileAlreadyExistException extends DownloadException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public FileAlreadyExistException(String message) {

        super(message);
    }

}
