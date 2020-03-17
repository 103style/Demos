package com.lxk.plugin.demo

import com.android.build.api.transform.*
import com.android.build.gradle.AppExtension
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import org.gradle.api.Plugin
import org.gradle.api.Project

import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

class DemoPlugin extends Transform implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.getByType(AppExtension)
        android.registerTransform(this)
    }

    @Override
    String getName() {
        return "DemoPlugin"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        println('----DemoPlugin START----')
        def sTime = System.currentTimeMillis()
        Collection<TransformInput> inputs = transformInvocation.inputs
        TransformOutputProvider outputProvider = transformInvocation.outputProvider
        if (outputProvider != null) {
            //清空之前的输出
            outputProvider.deleteAll()
        }
        //通过参数inputs可以拿到所有的class文件。
        // inputs中包括directoryInputs和jarInputs，
        // directoryInputs为文件夹中的class文件，
        // 而jarInputs为jar包中的class文件。
        inputs.each { TransformInput input ->
            ///遍历文件夹中的class文件
            input.directoryInputs.each { DirectoryInput directoryInput ->
                handleDirectoryInput(directoryInput, outputProvider)
            }

            //遍历jar包中的class文件
            input.jarInputs.each { JarInput jarInput ->
                handleJarInput(jarInput, outputProvider)
            }
        }
        println('----DemoPlugin END----')
        println("----DemoPlugin time cost = ${(System.currentTimeMillis() - sTime) / 1000} s ----")
    }

    void handleDirectoryInput(DirectoryInput directoryInput, TransformOutputProvider outputProvider) {
        if (directoryInput.file.isDirectory()) {
            directoryInput.file.eachFileRecurse { File file ->
                def name = file.name
                println("handleDirectoryInput -> file name = $name")
                if (checkClassName(name)) {
                    println("-----------handleDirectoryInput  deal with legal class file < $name > -----------")
                    modifyClassWithASM(file.bytes)
                }
            }
        }

        def dest = outputProvider.getContentLocation(directoryInput.name,
                directoryInput.contentTypes,
                directoryInput.scopes,
                Format.DIRECTORY)
        FileUtils.copyDirectory(directoryInput.file, dest)

    }

    static byte[] modifyClassWithASM(byte[] bytes) {
        // TODO ASM插桩
        return bytes
    }

    void handleJarInput(JarInput jarInput, TransformOutputProvider outputProvider) {
        if (jarInput.file.getAbsolutePath().endsWith(".jar")) {
            def jarName = jarInput.name
            def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
            if (jarName.endsWith(".jar")) {
                jarName = jarName.substring(0, jarName.length() - 4)
            }
            JarFile jarFile = new JarFile(jarInput.file)
            Enumeration enumeration = jarFile.entries()
            File tmpFile = new File(jarInput.file.getParent() + File.separator + "classes_temp.jar")
            //避免上次的缓存被重复插入
            if (tmpFile.exists()) {
                tmpFile.delete()
            }

            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(tmpFile))
            while (enumeration.hasMoreElements()) {
                JarEntry jarEntry = enumeration.nextElement()
                String entryName = jarEntry.name
                ZipEntry zipEntry = new ZipEntry(entryName)
                InputStream inputStream = jarFile.getInputStream(zipEntry)
                println("handleJarInput -> entryName = $entryName")
                //插桩class
                if (checkClassName(entryName)) {
                    //class文件处理
                    println '----------- handleJarInput deal with "jar" class file <' + entryName + '> -----------'
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(modifyClassWithASM(IOUtils.toByteArray(inputStream)))
                } else {
                    jarOutputStream.putNextEntry(zipEntry)
                    jarOutputStream.write(IOUtils.toByteArray(inputStream))
                }
                jarOutputStream.closeEntry()

            }
            //结束
            jarOutputStream.close()
            jarFile.close()
            def dest = outputProvider.getContentLocation(jarName + md5Name,
                    jarInput.contentTypes, jarInput.scopes, Format.JAR)
            FileUtils.copyFile(tmpFile, dest)
            tmpFile.delete()
        }
    }

    static boolean checkClassName(String name) {
        return name.endsWith(".class") &&
                !name.startsWith("R\$") &&
                !"R.class".startsWith(name) &&
                !"BuildConfig.class".startsWith(name) &&
                "android/support/v4/app/FragmentActivity.class".equals(name)
    }
}