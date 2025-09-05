# 📍 GeoMap Find & Tell

**GeoMap Find & Tell** is an Android application developed as a teamwork collaboration between two members.  
The app is designed to help communities **collaboratively locate and share resources** (such as fuel, sugar, etc.) by allowing users to place markers on a map that others can see in real time.  

- 🛠️ **Backend**: developed by me  
- 🎨 **Frontend**: developed by my teammate **Saif**

---

## ⚙️ Technologies Used

- **Firebase Realtime Database** – cloud-based backend for storing and syncing user data (secured as much as possible... had fun with the security rules.)
- **Google Maps API** – for displaying and interacting with map markers  
- **Android SDK** – `compileSdkVersion 32`  
- **Java (OpenJDK 11.0.2, 2019-01-15)** – primary programming language  
- **Android Studio** – development environment  

---

## 📱 Screenshots

### 🚀 Loading & Authentication
| Loading Screen | Bienvenue |
|----------------|-----------|
| ![Loading](https://github.com/user-attachments/assets/310c1dd3-5aea-46c2-b673-92c298070459) | ![Bienvenue](https://github.com/user-attachments/assets/8e03a4d8-6320-480d-8cc1-336c730ce0ec) |

---

### 🏠 Main Screen
![Main Screen](https://github.com/user-attachments/assets/797a4a67-5c89-4da2-acf0-1e4ddad857e7)

---

### 📂 Category Management
![Category Management](https://github.com/user-attachments/assets/50839c06-49dd-444d-939c-41c56fe10486)

---

### 📍 Add Marker
![Add Marker](https://github.com/user-attachments/assets/26b4202d-aa05-481a-aa15-d2ca48dc9bf1)

---

### 📍 Login
![Login](https://github.com/user-attachments/assets/7a4f5efa-87c2-4c98-9a54-a3b3d9abdade)

---

### 🔑 Register Variants
| Register 1 | Register 2 |
|---------|---------|
| ![Register1](https://github.com/user-attachments/assets/95553262-7d42-416b-a926-2108d1e206c9) | ![Register2](https://github.com/user-attachments/assets/3d9c7e20-07b0-46f0-a7d3-eb792f760a40) |

## Firebase security rules

```json
{
  "rules": {
    "categories": {
      ".read": true,  // anyone can read
      ".write": "auth != null && root.child('users').child(auth.uid).child('role').val() === 0" // only admin can edit or create
    },
    "markers": {
        ".read": true, // anyone can see markers
      "$country": {
        "$categoryId": {
          "$markerId": {

            ".write": "auth != null && (
                        // Admin can write anything
                        root.child('users').child(auth.uid).child('role').val() === 0 ||

                        // Creator can create a new marker where they are the creator
                        (!data.exists() && newData.child('creatorId').val() === auth.uid) ||

                        // Creator can edit their existing marker
                        (data.exists() && data.child('creatorId').val() === auth.uid)
                       )"
          }
        }
      }
    },

    "users": {
      "$uid": {
    ".read": "auth != null && auth.uid === $uid",  // users can read their own data

    ".write": "auth != null && (
                  // Admin can write anything, including role
                  root.child('users').child(auth.uid).child('role').val() === 0 ||

                  // User creating their own record for the first time, must have role = 1
                  (!data.exists() &&
                   newData.child('uid').val() === auth.uid &&
                   newData.child('role').val() === 1) ||

                  // User editing their own record, but cannot change role
                  (data.exists() &&
                   data.child('uid').val() === auth.uid &&
                   !newData.hasChild('role'))
               )"
      }
    }
  }
}
```

Other than this, you'll need three keys (in .local.properties) to initialize the firebase `MAPS_API_KEY` `MAPS_APP_ID` `MAPS_APP_URL`
