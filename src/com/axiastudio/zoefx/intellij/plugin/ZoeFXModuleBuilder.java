package com.axiastudio.zoefx.intellij.plugin;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.SourcePathsBuilder;
import com.intellij.openapi.module.*;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: tiziano
 * Date: 11/03/15
 * Time: 12:07
 */
public class ZoeFXModuleBuilder extends ModuleBuilder implements SourcePathsBuilder {

    private static final String TEMPLATES_PATH = "/com/axiastudio/zoefx/intellij/plugin/templates/";

    @Override
    public void setupRootModel(ModifiableRootModel modifiableRootModel) throws ConfigurationException {

        Sdk sdk = ProjectJdkTable.getInstance().getAllJdks()[0];
        modifiableRootModel.setSdk(sdk);

        final String moduleRootPath = getContentEntryPath();
        LocalFileSystem localFileSystem = LocalFileSystem.getInstance();
        VirtualFile moduleContentRoot = localFileSystem.refreshAndFindFileByPath(moduleRootPath);
        final ContentEntry rootContentEntry = modifiableRootModel.addContentEntry(moduleContentRoot);

        Project project = modifiableRootModel.getModule().getProject();
        VirtualFile baseDir = project.getBaseDir();

        try {
            // pom.xml
            Map<String, String> map = new HashMap<>();
            map.put("groupId", "com.yourdomain");
            map.put("artifactId", "myapp");
            VFUtil.createFile(baseDir, ZoeFXModuleBuilder.class.getResource(TEMPLATES_PATH + "pom_xml.txt"), "pom.xml", map);

            // folders
            VirtualFile src = VFUtil.createFolder(baseDir, "src");
            rootContentEntry.addSourceFolder(src, false);
            VirtualFile entities = VFUtil.createFolder(src, "entities");
            VirtualFile views = VFUtil.createFolder(src, "views");
            VirtualFile controllers = VFUtil.createFolder(src, "controllers");
            VirtualFile properties = VFUtil.createFolder(src, "properties");

            // Start.java
            VFUtil.createFile(src, ZoeFXModuleBuilder.class.getResource(TEMPLATES_PATH + "src/Start_java.txt"), "Start.java");

            // MyEntity.java
            VFUtil.createFile(entities, ZoeFXModuleBuilder.class.getResource(TEMPLATES_PATH + "src/entities/MyEntity_java.txt"), "MyEntity.java");

            // myentity.fxml
            VFUtil.createFile(views, ZoeFXModuleBuilder.class.getResource(TEMPLATES_PATH + "src/views/myentity_fxml.txt"), "myentity.fxml");

            // MyEntityController.java
            VFUtil.createFile(controllers, ZoeFXModuleBuilder.class.getResource(TEMPLATES_PATH + "src/controllers/MyEntityController_java.txt"), "MyEntityController.java");

            // myentity.properties
            VFUtil.createFile(properties, ZoeFXModuleBuilder.class.getResource(TEMPLATES_PATH + "src/properties/myentity_properties.txt"), "myentity.properties");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        //LibraryTable libraryTable = modifiableRootModel.getModuleLibraryTable();

    }

    @Override
    public ModuleType getModuleType() {
        return StdModuleTypes.JAVA;
    }

    @Override
    public List<Pair<String, String>> getSourcePaths() throws ConfigurationException {
        return null;
    }

    @Override
    public void setSourcePaths(List<Pair<String, String>> sourcePaths) {

    }

    @Override
    public void addSourcePath(Pair<String, String> sourcePathInfo) {

    }

    @Override
    public String getPresentableName() {
        return "ZoeFX Project";
    }

    @Override
    public String getDescription() {
        return "<html><br/><b>ZoeFX Application</b> creates a new IntelliJ project based on ZoeFX.</html>\n";
    }

}
