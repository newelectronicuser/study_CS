# Load Balancing and Caching

These are the primary tools used to scale systems and reduce latency.

## 1. Load Balancers
A Load Balancer (LB) distributes incoming network traffic across a group of backend servers.

### Types
- **Layer 4 (L4)**: Operates at the Transport level (TCP/UDP). It's fast but doesn't know the content of the request.
- **Layer 7 (L7)**: Operates at the Application level (HTTP). It can make routing decisions based on cookies, headers, or URL paths.

### Algorithms
- **Round Robin**: Requests are distributed sequentially.
- **Least Connections**: Sends requests to the server with the fewest active connections.
- **IP Hash**: Uses the client's IP address to determine which server receives the request (good for session persistence).

## 2. Caching
Storing copies of data in a high-speed storage layer (like RAM) so that future requests for that data can be served faster.

### Cache Placement
1.  **Client-side**: Browser cache.
2.  **CDN**: Content Delivery Network (Edge locations).
3.  **Web Server**: OpCache, local memory.
4.  **Database**: Buffer pool, query cache.
5.  **Application**: Distributed cache like Redis or Memcached.

### Invalidation Strategies
1.  **Write-through**: Data is written to the cache and the database simultaneously. (Consistent but slow).
2.  **Write-around**: Data is written only to the database. Cache is updated on a miss. (Fast, but risk of stale data).
3.  **Write-back (Write-behind)**: Data is written to the cache and asynchronously written to the database. (Very fast, risk of data loss on cache failure).

## 3. CDN (Content Delivery Network)
A distributed network of servers that deliver content to users based on their geographic location.
- **Pull CDN**: On the first request, the CDN pulls the content from the origin server and caches it.
- **Push CDN**: You proactively push the content to the CDN (best for content that doesn't change often).

> [!IMPORTANT]
> **The Thundering Herd Problem**: 
> Occurs when the cache expires for a very popular item, causing all incoming requests to hit the database at once. 
> **Solution**: Use Soft-TTL (expired items serve slightly stale data while one worker updates the cache) or Probabilistic Cache Filout.

> [!TIP]
> **The 80/20 Rule**: 
> 20% of the data usually generates 80% of the traffic. Focus on caching that high-volume 20%.
