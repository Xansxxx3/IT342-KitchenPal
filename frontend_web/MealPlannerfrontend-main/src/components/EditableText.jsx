import React, { useState } from "react";
import { TextField, Typography, Box, IconButton } from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";

const EditableText = ({ value, onSave, placeholder = "", sx = {} }) => {
  const [editing, setEditing] = useState(false);
  const [currentValue, setCurrentValue] = useState(value);

  const handleBlur = () => {
    setEditing(false);
    if (currentValue !== value) {
      onSave(currentValue);
    }
  };

  const handleKeyPress = (e) => {
    if (e.key === "Enter") {
      handleBlur();
    }
  };

  return editing ? (
    <TextField
      variant="standard"
      value={currentValue}
      onChange={(e) => setCurrentValue(e.target.value)}
      onBlur={handleBlur}
      onKeyPress={handleKeyPress}
      placeholder={placeholder}
      autoFocus
      InputProps={{
        disableUnderline: true,
        sx: {
          backgroundColor: "#f0f0f0",
          borderRadius: 1,
          px: 1,
          fontSize: "0.95rem",
          minWidth: "50px",
          '&:focus': {
            backgroundColor: "#e6f5ec",
          },
        },
      }}
      sx={{ ...sx }}
    />
  ) : (
    <Box
      onClick={() => setEditing(true)}
      sx={{
        display: "flex",
        alignItems: "center",
        cursor: "pointer",
        px: 1,
        py: 0.5,
        borderRadius: 1,
        transition: "background-color 0.2s",
        '&:hover': {
          backgroundColor: "#f5f5f5",
        },
        ...sx,
      }}
    >
      <Typography sx={{ fontSize: "0.95rem", mr: 0.5 }}>
        {value || placeholder || "Click to edit"}
      </Typography>
      <EditIcon fontSize="small" sx={{ color: "#999" }} />
    </Box>
  );
};

export default EditableText;
