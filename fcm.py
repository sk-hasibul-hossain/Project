import numpy as np
import cv2
from sklearn.metrics.pairwise import euclidean_distances


def fcm_image_segmentation(image, n_clusters, m, epsilon=1e-8, max_iter=100):
    img_flat = image.reshape(-1, 1)

    # Initialize cluster centers randomly
    centers = np.random.rand(n_clusters, img_flat.shape[1])

    for _ in range(max_iter):
        # Calculate distances with epsilon added to avoid zero values
        distances = euclidean_distances(img_flat, centers) + epsilon

        # Compute fuzzy memberships
        fuzzy_memberships = 1 / distances ** (2 / (m - 1))
        fuzzy_memberships /= np.sum(fuzzy_memberships, axis=1)[:, np.newaxis]

        # Update cluster centers
        centers_prev = centers.copy()
        centers = (fuzzy_memberships * m).T @ img_flat / \
            np.sum(fuzzy_memberships * m, axis=0)[:, np.newaxis]

        # Check for convergence
        

    # Reshape the membership matrix into the shape of the original image
    seg = np.argmax(fuzzy_memberships, axis=1).reshape(image.shape)

    return seg


# Example usage
image = cv2.imread('deba.jpeg', cv2.IMREAD_GRAYSCALE)
n_clusters = 3
m = 2
segmentation_result = fcm_image_segmentation(image, n_clusters, m)

# Display the original image and the segmentation result
cv2.imshow('Original Image', image)
cv2.imshow('Segmentation Result', segmentation_result.astype(
    np.uint8) * int(255 / (n_clusters - 1)))
cv2.waitKey(0)
cv2.destroyAllWindows()
