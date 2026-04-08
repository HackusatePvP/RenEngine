package me.piitex.os;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.*;

/**
 * Utility for GitHub REST API. Used for automatically updating and pulling information from releases and assets.
 */
public class GitHubUtil {
    private final String repositoryUrl;
    private static final Logger logger = LoggerFactory.getLogger(GitHubUtil.class);

    public GitHubUtil(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    public JSONObject getLatestReleaseJson() throws IOException, URISyntaxException {
        URL url = new URI(repositoryUrl + "releases/latest").toURL();
        logger.info("Fetching latest release request '{}'", url.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000);
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return new JSONObject(response.toString());
            }
        }
        return null;
    }

    public int getLatestReleaseID() throws IOException, URISyntaxException {
        JSONObject object = getLatestReleaseJson();
        return object != null ? object.getInt("id") : -1;
    }

    public JSONArray getReleaseAssets(int releaseID) throws IOException, URISyntaxException {
        URL url = new URI(repositoryUrl + "releases/" + releaseID + "/assets").toURL();
        logger.info("Fetching release asset '{}' '{}'", releaseID, url.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return new JSONArray(response.toString());
            }
        } else {
            throw new RuntimeException("GitHub API request failed. Response Code: " + connection.getResponseCode());
        }
    }

    public JSONObject getReleaseAsset(int releaseID, String pattern) throws IOException, URISyntaxException {
        JSONArray array = getReleaseAssets(releaseID);
        for (int i = 0; i < array.length(); i++) {
            JSONObject asset = array.getJSONObject(i);
            if (asset.getString("name").matches(pattern)) {
                return asset;
            }
        }
        return null;
    }

    public JSONObject getAsset(int assetId) throws IOException, URISyntaxException {
        URL url = new URI(repositoryUrl + "releases/assets/" + assetId).toURL();
        logger.info("Fetching asset '{}' '{}'", assetId, url.toString());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return new JSONObject(response.toString());
            }
        } else {
            throw new RuntimeException("GitHub API request failed. Response Code: " + connection.getResponseCode());
        }
    }

    public long getAssetFileSize(int assetId) throws URISyntaxException, IOException {
        return getAsset(assetId).getLong("size");
    }

    /**
     * Downloads an asset by ID. Extracts the hash directly from the asset's "digest" JSON field.
     */
    public FileDownloader downloadAsset(int assetId, File output, DownloadListener callback) {
        FileDownloader downloader = new FileDownloader();
        if (callback != null) {
            downloader.addDownloadListener(callback);
        }
        downloader.addRequestProperty("Accept", "application/octet-stream");

        String expectedHash = null;
        String hashAlgorithm = null;

        try {
            URL assetUrl = new URI(repositoryUrl + "releases/assets/" + assetId).toURL();
            HttpURLConnection assetConn = (HttpURLConnection) assetUrl.openConnection();
            assetConn.setRequestMethod("GET");
            assetConn.setRequestProperty("Accept", "application/vnd.github.v3+json");

            if (assetConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(assetConn.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) response.append(line);

                    JSONObject assetJson = new JSONObject(response.toString());
                    String digest = assetJson.optString("digest", "");

                    if (digest.contains(":")) {
                        String[] parts = digest.split(":", 2);
                        hashAlgorithm = formatAlgorithm(parts[0]);
                        expectedHash = parts[1];
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Failed to retrieve digest via GitHub API for asset ID {}. Proceeding without verification.", assetId, e);
        }

        // Only start download if a callback is present.
        if (callback != null) {
            if (expectedHash != null) {
                logger.info("Found {} hash for asset {}: {}", hashAlgorithm, assetId, expectedHash);
                downloader.startDownload(repositoryUrl + "releases/assets/" + assetId, output, expectedHash, hashAlgorithm);
            } else {
                logger.warn("No digest found for asset {}. Downloading without verification.", assetId);
                downloader.startDownload(repositoryUrl + "releases/assets/" + assetId, output);
            }
        }

        return downloader;
    }

    /**
     * Prepares a GitHub asset for download.
     */
    public DownloadInfo downloadAsset(int assetId, File output) throws URISyntaxException, IOException {
        return new DownloadInfo(output.getName(), output, getAssetFileSize(assetId), repositoryUrl + "releases/assets/" + assetId);
    }

    /**
     * Maps GitHub's algorithm string to standard Java MessageDigest algorithm names.
     */
    private String formatAlgorithm(String rawAlg) {
        String upperAlg = rawAlg.toUpperCase();
        return switch (upperAlg) {
            case "SHA256" -> "SHA-256";
            case "SHA512" -> "SHA-512";
            case "SHA1" -> "SHA-1";
            default -> upperAlg;
        };
    }
}