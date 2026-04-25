"""
Integration tests for the Pricing Engine
Tests the Java application by running Gradle as a subprocess
"""

import subprocess
import sys
import os

# المسار الجذري للمشروع
PROJECT_ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


def run_gradle_test():
    """تشغيل JUnit tests عبر Gradle"""
    gradlew = "gradlew.bat" if sys.platform == "win32" else "./gradlew"
    gradlew_path = os.path.join(PROJECT_ROOT, gradlew)
    
    result = subprocess.run(
        [gradlew_path, "test"],
        cwd=PROJECT_ROOT,
        capture_output=True,
        text=True,
        shell=True
    )
    return result


def test_project_structure():
    """التأكد من وجود الملفات الأساسية"""
    print("📁 Checking project structure...")
    
    required_files = [
        "build.gradle",
        "settings.gradle",
        "src/main/java/pricing/PricingEngine.java",
        "src/main/java/pricing/CustomerType.java",
        "src/main/java/pricing/DiscountCode.java",
        "src/main/java/pricing/PriceBreakdown.java",
        "src/test/java/pricing/PricingEngineTest.java",
    ]
    
    missing = []
    for file_path in required_files:
        full_path = os.path.join(PROJECT_ROOT, file_path)
        if os.path.exists(full_path):
            print(f"  ✓ {file_path}")
        else:
            print(f"  ✗ {file_path} (MISSING)")
            missing.append(file_path)
    
    assert not missing, f"Missing files: {missing}"
    print("✅ Project structure is correct\n")


def test_gradle_build_succeeds():
    """التأكد من أن المشروع يُبنى بنجاح"""
    print("🔨 Testing Gradle build...")
    result = run_gradle_test()
    
    if result.returncode != 0:
        print(f"STDOUT:\n{result.stdout}")
        print(f"STDERR:\n{result.stderr}")
    
    assert result.returncode == 0, "Gradle build failed"
    print("✅ Gradle build succeeded\n")


def test_all_junit_tests_pass():
    """التأكد من نجاح كل JUnit tests"""
    print("🧪 Running JUnit tests via Gradle...")
    result = run_gradle_test()
    
    output = result.stdout + result.stderr
    
    assert "BUILD SUCCESSFUL" in output, f"Tests failed:\n{output}"
    assert "FAILED" not in output, f"Some tests failed:\n{output}"
    
    print("✅ All JUnit tests passed\n")


def test_gitignore_exists():
    """التأكد من وجود .gitignore"""
    print("📝 Checking .gitignore...")
    gitignore_path = os.path.join(PROJECT_ROOT, ".gitignore")
    assert os.path.exists(gitignore_path), ".gitignore is missing"
    print("✅ .gitignore exists\n")


def test_readme_exists():
    """التأكد من وجود README.md"""
    print("📖 Checking README.md...")
    readme_path = os.path.join(PROJECT_ROOT, "README.md")
    assert os.path.exists(readme_path), "README.md is missing"
    print("✅ README.md exists\n")


if __name__ == "__main__":
    print("=" * 60)
    print("🚀 Running Integration Tests for Pricing Engine")
    print("=" * 60 + "\n")
    
    try:
        test_project_structure()
        test_gitignore_exists()
        test_readme_exists()
        test_gradle_build_succeeds()
        test_all_junit_tests_pass()
        
        print("=" * 60)
        print("🎉 All integration tests passed successfully!")
        print("=" * 60)
        sys.exit(0)
    except AssertionError as e:
        print(f"\n❌ Test failed: {e}")
        sys.exit(1)
    except Exception as e:
        print(f"\n💥 Unexpected error: {e}")
        sys.exit(1)