package com.axiastudio.zoefx.intellij.plugin;

import com.axiastudio.mapformat.MessageMapFormat;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * User: tiziano
 * Date: 12/03/15
 * Time: 14:46
 */
public class VFUtil {

    public static VirtualFile createFile(VirtualFile dir, URL url, String name) throws IOException, URISyntaxException {
        return createFile(dir, url, name, null);
    }

    public static VirtualFile createFile(VirtualFile folder, URL url, String fileName, Map<String, String> map) throws IOException, URISyntaxException {
        VirtualFile file = folder.createChildData(ZoeFXModuleBuilder.class, fileName);
        OutputStream outputStream = file.getOutputStream(ZoeFXModuleBuilder.class);
        String content = new String(Files.readAllBytes(Paths.get(url.toURI())));
        if( map != null ){
            MessageMapFormat mmp = new MessageMapFormat(content);
            content = mmp.format(map);
        }
        ByteArrayInputStream inputStream = new ByteArrayInputStream(content.getBytes());
        FileUtil.copy(inputStream, outputStream);
        outputStream.flush();
        outputStream.close();
        return file;
    }

    public static VirtualFile createFolder(VirtualFile parentFolder, String folderName) throws IOException {
        return parentFolder.createChildDirectory(ZoeFXModuleBuilder.class, folderName);
    }
}
