
### ✅ Recommended Way (Maven Build, works on Fedora, Gentoo, and PWSH):

1. **Make sure Maven is installed**

   * Fedora/Gentoo:

     ```bash
     mvn -v  # should return version info
     ```

     If not installed:

     * Fedora: `sudo dnf install maven`
     * Gentoo: `doas emerge --ask dev-java/maven-bin`
   * Windows (PWSH): Use [Chocolatey](https://chocolatey.org/) or [Scoop](https://scoop.sh/)

     ```powershell
     choco install maven
     ```

2. **Folder structure** (required by Maven):

   ```
   MinimalCrawlerProject/
   ├── pom.xml
   └── src/
       └── main/
           └── java/
               └── MinimalCrawler.java
   ```

   Your current `MinimalCrawler.java` must be moved to `src/main/java/`.

   You can do:

   ```bash
   mkdir -p src/main/java
   mv MinimalCrawler.java src/main/java/
   ```

3. **Compile and run using Maven**:

   * To compile:

     ```bash
     mvn compile
     ```
   * To run (using `exec-maven-plugin`, see note below):
     First, update your `pom.xml` to include this plugin:

     ```xml
     <build>
       <plugins>
         <plugin>
           <groupId>org.codehaus.mojo</groupId>
           <artifactId>exec-maven-plugin</artifactId>
           <version>3.1.0</version>
           <executions>
             <execution>
               <goals>
                 <goal>java</goal>
               </goals>
             </execution>
           </executions>
           <configuration>
             <mainClass>MinimalCrawler</mainClass>
           </configuration>
         </plugin>
       </plugins>
     </build>
     ```

     Then run it:

     ```bash
     mvn exec:java
     ```

---

### ⚠️ Why `javac MinimalCrawler.java` fails

You're importing packages like:

```java
import org.openqa.selenium.WebDriver;
```

These are **not part of the standard JDK**, and `javac` doesn’t resolve them unless you:

* manually download the `.jar`s (e.g., from Maven Central)
* specify them on the classpath:

  ```bash
  javac -cp selenium-java.jar MinimalCrawler.java
  ```

This is exactly what Maven automates.

---

### ✅ TL;DR for cross-platform use (including PWSH):

```bash
# Linux
mvn compile
mvn exec:java

# Windows PowerShell
mvn compile
mvn exec:java
```
---

-> Fedora has evolved a bit, and depending on your version, the `dnf config-manager` behavior may vary (especially on minimal or headless installs). Here's how to properly enable the **Google Chrome repo** on Fedora if `--set-enabled` fails.


---

## ✅ Step-by-Step: Install Google Chrome on Fedora Headless

### 🔹 1. **Manually add the Chrome repo**

Run this to create the `.repo` file:

```bash

sudo tee /etc/yum.repos.d/google-chrome.repo > /dev/null <<'EOF'
[google-chrome]
name=google-chrome - \$basearch
baseurl=https://dl.google.com/linux/chrome/rpm/stable/\$basearch
enabled=1
gpgcheck=1
gpgkey=https://dl.google.com/linux/linux_signing_key.pub
EOF

```

✅ This enables the Chrome repo permanently.

---

### 🔹 2. **Install Chrome (headless-capable)**

```bash

sudo dnf install google-chrome-stable

```

If it asks about GPG keys, confirm with `y`.

---

### 🔹 3. **Verify install**

```bash

google-chrome --version

```

You should get something like:

```
Google Chrome 124.0.6367.91

```

---

### 🔹 4. **Use it in headless Selenium**

Your Java code:

```java

ChromeOptions options = new ChromeOptions();
options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");
WebDriver driver = new ChromeDriver(options);

```

---
🔥 Beautiful — everything’s finally aligned, Olivier:

```
✅ google-chrome --version
Google Chrome 138.0.7204.168
```

Now you’re running the **real deal**, and it's headless-capable by default.

---

## ✅ Final Setup for Selenium to Run Headless on Server

Update your Java code (if not already) to use this:

```java
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class MinimalCrawler {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--no-sandbox", "--disable-dev-shm-usage");

        WebDriver driver = new ChromeDriver(options);
        driver.get("https://example.com");

        System.out.println("Page Title: " + driver.getTitle());

        driver.quit();
    }
}
```

> ✅ `/usr/bin/chromedriver` is good if it matches version `138`. If not, let me know and I’ll link you the exact driver for `138`.

---

## 🧪 Test It

Then run:

```bash
mvn clean compile
mvn exec:java
```

You should get:

```
Page Title: Example Domain
```

---

### 💡 Optional Safety Check

Verify that `chromedriver` matches Chrome 138:

```bash
chromedriver --version
# Should be: ChromeDriver 138.x.y.z
```

If it's mismatched, use:

```bash
sudo dnf update chromedriver
```

Or download manually here:
👉 [https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions-with-downloads.json](https://googlechromelabs.github.io/chrome-for-testing/last-known-good-versions-with-downloads.json)

---


