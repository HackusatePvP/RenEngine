package me.piitex.os;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Utility for GitHub REST API. Used for automatically updating and pulling information from releases and assets.
 */
public class GitHubUtil {
    private final String repositoryUrl; //https://api.github.com/repos/owner/repo/

    /**
     * Initializes the base url for the repository. To get the repository link, use the following format.
     * <pre>
     *     https://api.github.com/repos/$OWNER/$REPO/
     * </pre>
     * @param repositoryUrl The GitHub REST api repository link.
     */
    public GitHubUtil(String repositoryUrl) {
        this.repositoryUrl = repositoryUrl;
    }

    /**
     * Gathers the latest release of the repository into a {@link JSONObject}.
     * @return {@link JSONObject} of the latest release.
     * @throws IOException If a connection cannot be made.
     */
    public JSONObject getLatestReleaseJson() throws IOException, URISyntaxException {
        URL url = new URI(repositoryUrl + "releases/latest").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(1000);
        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
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
        return object.getInt("id");
    }

    public JSONArray getReleaseAssets(int releaseID) throws IOException, URISyntaxException {
        URL url = new URI(repositoryUrl + "releases/" + releaseID + "/assets").toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                return new JSONArray(response.toString());
            }
        } else {
            throw new RuntimeException("GitHub API request failed. Response Code: " + responseCode);
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

    public FileDownloader downloadAsset(int assetId, File output, DownloadListener callback) {
        FileDownloader downloader = new FileDownloader();
        downloader.addDownloadListener(callback);
        downloader.addRequestProperty("Accept", "application/octet-stream");
        downloader.startDownload(repositoryUrl + "releases/" + "assets/" + assetId, output);
        return downloader;
    }
}
