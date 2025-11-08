# 🚀 BAAHUBALI Bank - Production Deployment Guide

Your application is now **production-ready** for deployment to any cloud platform!

## 📋 Pre-Deployment Checklist

- ✅ MongoDB Atlas cluster configured
- ✅ Application builds successfully (`mvn clean package`)
- ✅ Runs locally on port 8080
- ✅ Environment variables configured
- ✅ Security settings reviewed
- ✅ Monitoring endpoints enabled (Actuator)

---

## 🌐 Environment Variables Required

Set these environment variables on your deployment platform:

### **Required**
```bash
MONGODB_URI=mongodb+srv://username:password@cluster.mongodb.net/dbname?retryWrites=true&w=majority
PORT=8080  # Usually set automatically by platform
```

### **Optional (Recommended)**
```bash
MONGODB_DATABASE=baahubali-bank
LOG_LEVEL=INFO
APP_LOG_LEVEL=INFO
SECURITY_PASSWORD=your-secure-admin-password
SPRING_PROFILES_ACTIVE=prod
```

---

## 🚀 Platform-Specific Deployment

### **1. Railway** (Recommended - Easiest)

#### Step 1: Install Railway CLI or use Web Dashboard
```bash
npm install -g @railway/cli
railway login
```

#### Step 2: Initialize Project
```bash
railway init
```

#### Step 3: Add MongoDB Plugin
- Go to Railway Dashboard
- Click "New" → "Database" → "MongoDB"
- Railway automatically sets `MONGODB_URI`

#### Step 4: Set Environment Variables
```bash
railway variables set MONGODB_DATABASE=baahubali-bank
railway variables set LOG_LEVEL=INFO
```

#### Step 5: Deploy
```bash
railway up
```

**Your app URL:** `https://your-app.up.railway.app`

---

### **2. Render**

#### Step 1: Create Web Service
- Go to [Render Dashboard](https://dashboard.render.com/)
- Click "New" → "Web Service"
- Connect your GitHub repository

#### Step 2: Configure Build
- **Build Command:** `mvn clean package`
- **Start Command:** `java -jar target/bank-mvp-0.0.1-SNAPSHOT.jar`
- **Environment:** Java

#### Step 3: Add Environment Variables
```bash
MONGODB_URI=mongodb+srv://...
MONGODB_DATABASE=baahubali-bank
PORT=8080
```

#### Step 4: Connect MongoDB Atlas
- Use your existing MongoDB Atlas connection string
- Or create a new MongoDB database on Render

#### Step 5: Deploy
- Click "Create Web Service"
- Automatic deployments on git push

**Health Check Endpoint:** `https://your-app.onrender.com/actuator/health`

---

### **3. Heroku**

#### Step 1: Install Heroku CLI
```bash
# Windows (with Chocolatey)
choco install heroku-cli

# Or download from heroku.com
```

#### Step 2: Login and Create App
```bash
heroku login
heroku create baahubali-bank-app
```

#### Step 3: Add Buildpack
```bash
heroku buildpacks:set heroku/java
```

#### Step 4: Set Environment Variables
```bash
heroku config:set MONGODB_URI="mongodb+srv://..."
heroku config:set MONGODB_DATABASE=baahubali-bank
heroku config:set LOG_LEVEL=INFO
```

#### Step 5: Deploy
```bash
git push heroku main
```

#### Step 6: Open App
```bash
heroku open
```

**Logs:** `heroku logs --tail`

---

### **4. AWS Elastic Beanstalk**

#### Step 1: Install EB CLI
```bash
pip install awsebcli
```

#### Step 2: Initialize
```bash
eb init -p java-21 baahubali-bank
```

#### Step 3: Create Environment
```bash
eb create baahubali-bank-prod
```

#### Step 4: Set Environment Variables
```bash
eb setenv MONGODB_URI="mongodb+srv://..." \
          MONGODB_DATABASE=baahubali-bank \
          LOG_LEVEL=INFO
```

#### Step 5: Deploy
```bash
mvn clean package
eb deploy
```

---

### **5. Docker Deployment**

#### Create `Dockerfile`:
```dockerfile
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/bank-mvp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Build and Run:
```bash
# Build
mvn clean package
docker build -t baahubali-bank .

# Run locally
docker run -p 8080:8080 \
  -e MONGODB_URI="mongodb+srv://..." \
  -e MONGODB_DATABASE=baahubali-bank \
  baahubali-bank

# Push to Docker Hub
docker tag baahubali-bank yourusername/baahubali-bank
docker push yourusername/baahubali-bank
```

---

## 🔒 Production Security Checklist

### **MongoDB Atlas**
- ✅ Enable authentication
- ✅ Whitelist only your app's IPs (or use 0.0.0.0/0 for cloud platforms)
- ✅ Use strong passwords
- ✅ Enable audit logs
- ✅ Regular backups enabled

### **Application**
- ✅ Change default admin password: `SECURITY_PASSWORD` env var
- ✅ Use HTTPS (platforms like Railway/Render provide this automatically)
- ✅ Review CORS settings in `SecurityConfig.java`
- ✅ Enable rate limiting (consider adding Spring Cloud Gateway)
- ✅ Set `LOG_LEVEL=WARN` or `ERROR` in production

### **Environment Variables**
- ✅ Never commit secrets to Git
- ✅ Use platform's secret management
- ✅ Rotate passwords regularly

---

## 📊 Monitoring & Health Checks

### **Health Check Endpoints** (Actuator)

**Basic Health:**
```bash
curl https://your-app.com/actuator/health
```

**Detailed Health (when authorized):**
```bash
curl https://your-app.com/actuator/health/mongo
```

**Application Info:**
```bash
curl https://your-app.com/actuator/info
```

### **Set Up Monitoring**

1. **Railway:** Built-in metrics dashboard
2. **Render:** Built-in monitoring
3. **Heroku:** Use Heroku Metrics or New Relic addon
4. **Custom:** Integrate with Datadog, Prometheus, or Grafana

---

## 🧪 Testing Production Deployment

### 1. **Check Health Endpoint**
```bash
curl https://your-app.com/actuator/health
```

Expected Response:
```json
{
  "status": "UP",
  "components": {
    "mongo": {
      "status": "UP"
    }
  }
}
```

### 2. **Test Login**
```bash
curl -X POST https://your-app.com/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@baahubali.com",
    "password": "admin123"
  }'
```

### 3. **Check Database Connection**
- Open MongoDB Atlas Dashboard
- Navigate to Database → Collections
- Verify `baahubali-bank` database exists
- Check `users` collection has seed data

---

## 🔄 Continuous Deployment (CI/CD)

### **GitHub Actions Example**

Create `.github/workflows/deploy.yml`:

```yaml
name: Deploy to Production

on:
  push:
    branches: [ main ]

jobs:
  deploy:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 21
      uses: actions/setup-java@v3
      with:
        java-version: '21'
        distribution: 'temurin'
    
    - name: Build with Maven
      run: mvn clean package
    
    - name: Deploy to Railway
      env:
        RAILWAY_TOKEN: ${{ secrets.RAILWAY_TOKEN }}
      run: |
        npm i -g @railway/cli
        railway up --service your-service-id
```

---

## 📝 Environment Variables Reference

| Variable | Required | Default | Description |
|----------|----------|---------|-------------|
| `PORT` | No | 8080 | Server port (set by platform) |
| `MONGODB_URI` | Yes | - | MongoDB connection string |
| `MONGODB_DATABASE` | No | baahubali-bank | Database name |
| `LOG_LEVEL` | No | INFO | Root logging level |
| `APP_LOG_LEVEL` | No | INFO | Application logging level |
| `SECURITY_PASSWORD` | No | change-me | Admin password |
| `SPRING_PROFILES_ACTIVE` | No | - | Active profile (e.g., prod) |

---

## 🐛 Troubleshooting

### **Connection Issues**
```bash
# Check if MongoDB is accessible
curl -I https://your-app.com/actuator/health

# Check logs
# Railway: railway logs
# Render: View logs in dashboard
# Heroku: heroku logs --tail
```

### **Build Failures**
```bash
# Ensure Java 21 is being used
mvn -version

# Clean rebuild
mvn clean install -U
```

### **MongoDB Connection Failed**
- Verify IP whitelist in MongoDB Atlas (0.0.0.0/0 for cloud platforms)
- Check connection string format
- Verify username/password are correct
- Ensure special characters in password are URL-encoded

---

## 🎯 Post-Deployment Steps

1. **Test All Features**
   - User registration/login
   - Deposits/withdrawals
   - Money transfers
   - Loan applications
   - Admin dashboard

2. **Set Up Monitoring**
   - Configure uptime monitoring (UptimeRobot, Pingdom)
   - Set up error tracking (Sentry)
   - Enable log aggregation

3. **Create Backups**
   - Enable MongoDB Atlas automated backups
   - Test restore procedure

4. **Document**
   - Save deployment URL
   - Document environment variables
   - Create runbook for common issues

---

## 📞 Support & Resources

- **MongoDB Atlas:** https://www.mongodb.com/docs/atlas/
- **Spring Boot:** https://spring.io/projects/spring-boot
- **Railway:** https://docs.railway.app/
- **Render:** https://render.com/docs
- **Heroku:** https://devcenter.heroku.com/

---

## ✅ Success Checklist

- [ ] Application deployed successfully
- [ ] Health endpoint returns 200 OK
- [ ] MongoDB connection verified
- [ ] Test accounts working
- [ ] HTTPS enabled
- [ ] Environment variables secured
- [ ] Monitoring enabled
- [ ] Backups configured
- [ ] Domain configured (optional)
- [ ] Documentation updated

---

**Your BAAHUBALI Bank is now LIVE! 🎉**

Test URL: `https://your-app-name.platform.com`
