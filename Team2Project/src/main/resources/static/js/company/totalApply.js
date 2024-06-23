function updateProfileCheck(applyKey) {
            $.ajax({
                type: "POST",
                url: "/ajax/updateProfileCheck",
                data: { applyKey: applyKey },
                success: function() {
                    console.log();
                },
                error: function() {
                    alert("Error updating profile check");
                }
            });
        }