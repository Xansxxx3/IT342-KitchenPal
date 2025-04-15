import React, { useState, useEffect } from "react";
import axios from "axios";
import "../CSS/UserProfile.css";
import NavBar from '../components/NavBar';
import Cover from '../assets/Cover.png';
import {
    Box,
    Container,
    Typography,
    Grid,
    Card,
    CardMedia,
    CardContent,
    CardActionArea,
    CircularProgress,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
    Button,
  } from '@mui/material';
const UserProfile = () => {
    const [profile, setProfile] = useState({
        fname: "",
        lname: "",
        email: "",
    });

    const [passwordData, setPasswordData] = useState({
        currentPassword: "",
        newPassword: "",
    });

    const [message, setMessage] = useState("");
    const [passwordMessage, setPasswordMessage] = useState("");

    useEffect(() => {
        const fetchUserProfile = async () => {
            const token = localStorage.getItem("token");

            if (!token) {
                setMessage("No token found. Please log in.");
                return;
            }

            try {
                const response = await axios.get("http://localhost:8080/api/v1/auth/me", {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });

                setProfile(response.data);
            } catch (error) {
                setMessage("Error fetching profile data.");
            }
        };

        fetchUserProfile();
    }, []);

    const handleProfileChange = (e) => {
        const { name, value } = e.target;
        setProfile((prevProfile) => ({
            ...prevProfile,
            [name]: value,
        }));
    };

    const handlePasswordChange = (e) => {
        const { name, value } = e.target;
        setPasswordData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleProfileSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.put(
                "http://localhost:8080/api/v1/user/profile",
                profile,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            console.log(response.data); 
            setMessage("Profile updated successfully!");
        } catch (error) {
            console.error(error.response || error.message); 
            setMessage(error.response?.data?.message || "Error updating profile.");
        }
    };

    const handlePasswordSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.put(
                "http://localhost:8080/api/v1/user/change-password",
                passwordData,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("token")}`,
                        "Content-Type": "application/json",
                    },
                }
            );
            console.log(response.data); 
            setPasswordMessage("Password updated successfully!");
            setPasswordData({
                currentPassword: "",
                newPassword: "",
            }); 
        } catch (error) {
            console.error(error.response || error.message); 
            setPasswordMessage(error.response?.data?.message || "Error updating password.");
        }
    };
    
        return (
            <div>
            {/* NavBar Component */}
            <NavBar />

            {/* Content with Background */}
            <div
                className="profile-background"
                style={{
                    backgroundImage: `url(${Cover})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    filter: 'brightness(0.8)', // Slightly darkens the image
                }}
            >
                {/* Profile and Password Forms */}
                <div className="profile-container">
                    <h2>Edit Profile</h2>
                    {message && <p className="message">{message}</p>}
                    <form onSubmit={handleProfileSubmit} className="profile-form">
                        <div className="form-group">
                            <label htmlFor="fname">First Name</label>
                            <input
                                type="text"
                                id="fname"
                                name="fname"
                                value={profile.fname}
                                onChange={handleProfileChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="lname">Last Name</label>
                            <input
                                type="text"
                                id="lname"
                                name="lname"
                                value={profile.lname}
                                onChange={handleProfileChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="email">Email</label>
                            <input
                                type="email"
                                id="email"
                                name="email"
                                value={profile.email}
                                onChange={handleProfileChange}
                                required
                            />
                        </div>
                        <button type="submit" className="submit-btn">Update Profile</button>
                    </form>
                </div>

                <div className="password-container">
                    <h2>Change Password</h2>
                    {passwordMessage && <p className="message">{passwordMessage}</p>}
                    <form onSubmit={handlePasswordSubmit} className="password-form">
                        <div className="form-group">
                            <label htmlFor="currentPassword">Current Password</label>
                            <input
                                type="password"
                                id="currentPassword"
                                name="currentPassword"
                                value={passwordData.currentPassword}
                                onChange={handlePasswordChange}
                                required
                            />
                        </div>
                        <div className="form-group">
                            <label htmlFor="newPassword">New Password</label>
                            <input
                                type="password"
                                id="newPassword"
                                name="newPassword"
                                value={passwordData.newPassword}
                                onChange={handlePasswordChange}
                                required
                            />
                        </div>
                        <button type="submit" className="submit-btn">Change Password</button>
                    </form>
                </div>
            </div>
        </div>
    );
};

export default UserProfile;
