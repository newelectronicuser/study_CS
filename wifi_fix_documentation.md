# Documentation: Wi-Fi Stability Fix (Realtek RTL8852BE)

**Date:** 2026-03-31
**System:** Ubuntu 24.04 (Noble Numbat)
**Hardware:** Realtek RTL8852BE PCIe 802.11ax Wireless Network Controller
**Issue:** Intermittent connection drops requiring manual reconnect/refresh.

---

## 1. Problem Identification

The root cause was identified as aggressive **Power Management** settings in both the **NetworkManager** service and the **rtw89** kernel driver. This specific Realtek chipset is known to exhibit instability when entering or exiting low-power states (ASPM and PS modes) on Linux.

---

## 2. Technical Diagnostic

- **OS Version:** Ubuntu 24.04
- **Kernel Version:** 6.17.0-19-generic
- **Driver:** `rtw89_8852be`
- **Initial State:** `Power Management:on` (verified via `iwconfig`)
- **NetworkManager Config:** `wifi.powersave = 3` (Enabled)

---

## 3. Implementation Details

### Configuration A: NetworkManager Fix
We changed the global Wi-Fi power-saving policy to ensure the system does not attempt to put the card to sleep during inactivity.

- **File:** `/etc/NetworkManager/conf.d/default-wifi-powersave-on.conf`
- **Content:**
  ```ini
  [connection]
  wifi.powersave = 2
  ```

### Configuration B: Kernel Driver Fix (ASPM & PS Mode)
We disabled **Active State Power Management (ASPM)** and the internal low-power mode of the driver to prevent hardware-level disconnects.

- **File:** `/etc/modprobe.d/rtw89.conf`
- **Content:**
  ```conf
  options rtw89_pci disable_aspm_l1=y
  options rtw89_pci disable_aspm_l1ss=y
  options rtw89_core disable_ps_mode=y
  ```

---

## 4. Verification

- **Command Run:** `iwconfig wlp8s0`
- **Result:** `Power Management:off` (Active)
- **Command Run:** `sudo systemctl restart NetworkManager`
- **Result:** Service restarted successfully; connection re-established with new policy.

---

## 5. Maintenance and Recovery

> [!TIP]
> **How to Revert Changes:**
> 1. Delete the modprobe file: `sudo rm /etc/modprobe.d/rtw89.conf`
> 2. Restore NetworkManager setting: `echo -e "[connection]\nwifi.powersave = 3" | sudo tee /etc/NetworkManager/conf.d/default-wifi-powersave-on.conf`
> 3. Reboot the system.

> [!IMPORTANT]
> **A system reboot is required** for the driver settings in `/etc/modprobe.d/rtw89.conf` to be fully loaded into the hardware.
