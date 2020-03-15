package com.vandamodaintima.jfpsb.contador.sincronizacao;

import org.threeten.bp.ZonedDateTime;

import java.util.Objects;

public class DatabaseLogFileInfo {
    private String FileName;
    private ZonedDateTime LastModified;

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        this.FileName = fileName;
    }

    public ZonedDateTime getLastModified() {
        return LastModified;
    }

    public void setLastModified(ZonedDateTime lastModified) {
        this.LastModified = lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DatabaseLogFileInfo that = (DatabaseLogFileInfo) o;
        return FileName.equals(that.FileName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(FileName);
    }
}
