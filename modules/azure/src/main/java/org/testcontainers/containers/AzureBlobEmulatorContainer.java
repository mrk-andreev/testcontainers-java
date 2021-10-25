package org.testcontainers.containers;

import java.util.Base64;
import java.util.concurrent.ThreadLocalRandom;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

/**
 * An Azure Storage Blob container
 */
public class AzureBlobEmulatorContainer extends GenericContainer<AzureBlobEmulatorContainer> {

    private static final DockerImageName DEFAULT_IMAGE_NAME =
            DockerImageName.parse("mcr.microsoft.com/azure-blob-storage");

    private static final int KEY_LENGTH = 64;
    private static final int PORT = 11002;
    private final String storageAccountKey;
    private final String storageAccountName;

    /**
     * @param dockerImageName specified docker image name to run
     */
    public AzureBlobEmulatorContainer(final DockerImageName dockerImageName) {
        super(dockerImageName);
        storageAccountKey = generateStorageAccountKey();
        storageAccountName = generateStorageAccountName();

        dockerImageName.assertCompatibleWith(DEFAULT_IMAGE_NAME);
        withExposedPorts(PORT);
        withEnv("LOCAL_STORAGE_ACCOUNT_KEY", storageAccountKey);
        withEnv("LOCAL_STORAGE_ACCOUNT_NAME", storageAccountName);
        waitingFor(Wait.forLogMessage("(?s).*StartAsync completed.*", 1));
    }

    public String getStorageAccountKey() {
        return storageAccountKey;
    }

    public String getStorageAccountName() {
        return storageAccountName;
    }

    private String generateStorageAccountKey() {
        byte[] key = new byte[KEY_LENGTH];
        ThreadLocalRandom.current().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    private String generateStorageAccountName() {
        return "bucket";
    }
}
