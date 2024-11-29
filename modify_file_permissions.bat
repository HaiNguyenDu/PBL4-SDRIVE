@echo off
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /grant "PBL4\Administrator:F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /grant "PBL4\XPhuc:F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /inheritance:r
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /grant "PBL4\Administrator:F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /grant "PBL4\XPhuc:F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /remove "PBL4\Thanhan"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /grant "PBL4\Thanhan":M
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /grant "PBL4\Administrator:F"
icacls "\\10.10.37.25\SDriver\XPhuc\An_soai_ca\doc.txt" /grant "PBL4\XPhuc:F"
